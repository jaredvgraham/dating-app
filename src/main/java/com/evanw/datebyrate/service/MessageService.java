package com.evanw.datebyrate.service;

import com.evanw.datebyrate.Dto.match.MatchDto;
import com.evanw.datebyrate.Dto.message.MessageDto;
import com.evanw.datebyrate.exception.MatchNotFoundException;
import com.evanw.datebyrate.exception.MessageException;
import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.mappers.MessageMapper;
import com.evanw.datebyrate.models.Match;
import com.evanw.datebyrate.models.Message;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.MatchRepository;
import com.evanw.datebyrate.repository.MessageRepository;
import com.evanw.datebyrate.repository.UserRepository;
import com.evanw.datebyrate.utils.CryptoUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private MatchRepository matchRepository;
    private MessageMapper messageMapper;
    private CryptoUtils cryptoUtils;
    private UserRepository userRepository;

    public MessageService(
            MessageRepository messageRepository,
            MatchRepository matchRepository,
            MessageMapper messageMapper,
            CryptoUtils cryptoUtils,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.matchRepository = matchRepository;
        this.messageMapper = messageMapper;
        this.cryptoUtils = cryptoUtils;
        this.userRepository = userRepository;
    }

    @Transactional
    public MessageDto save(String content, Integer senderId, String matchKey) {
        Optional<Match> matchOpt = matchRepository.findMatchByKey(matchKey, senderId);
        if (matchOpt.isEmpty()) {
            throw new MatchNotFoundException("No match found");
        }
        Match match = matchOpt.get();
        Integer receiverId = match.getMatchedUser().getId();

        Message message = new Message();
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setMatchKey(matchKey);
        messageRepository.save(message);

        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getTimestamp(),
                senderId,
                receiverId,
                message.isRead()
        );
    }

    @Transactional
    public List<MessageDto> getMessagesByMatchKey(String matchKey){
        return messageRepository.findMessageByMatchKey(matchKey)
                .stream()
                .map(messageMapper::toMessageDtoFromMessage)
                .collect(Collectors.toList());
    }

    @Transactional
    public void readMessage(Integer messageId, Authentication auth){
        String username = auth.getName();
        Message message = getMessageById(messageId);
        User user = getUserByUsername(username);
        Integer userId = user.getId();
        String matchKey = message.getMatchKey();
        LocalDateTime timestamp = message.getTimestamp();
        messageRepository.setAllPreviousMessagesRead(
                matchKey,
                timestamp,
                userId
        );
    }

    private Message getMessageById(Integer messageId){
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new MessageException("No message found with id " + messageId);
        }
        return messageOpt.get();
    }
    private User getUserByUsername(String username){
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()){
            throw new UserNotFoundException("No user found with username " + username);
        }
        return userOpt.get();
    }
}
