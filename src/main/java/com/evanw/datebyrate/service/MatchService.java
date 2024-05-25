package com.evanw.datebyrate.service;

import com.evanw.datebyrate.Dto.match.MatchDto;
import com.evanw.datebyrate.Dto.message.MessageConvoDto;
import com.evanw.datebyrate.exception.*;
import com.evanw.datebyrate.mappers.MatchMapper;
import com.evanw.datebyrate.models.Image;
import com.evanw.datebyrate.models.Match;
import com.evanw.datebyrate.models.Message;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.MatchRepository;
import com.evanw.datebyrate.repository.MessageRepository;
import com.evanw.datebyrate.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private MatchRepository matchRepository;
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private MatchMapper matchMapper;

    public MatchService(
            MatchRepository matchRepository,
            UserRepository userRepository,
            MessageRepository messageRepository,
            MatchMapper matchMapper
    ) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.matchMapper = matchMapper;
    }

    @Cacheable(value = "matchesCache", key = "#username")
    @Transactional
    public List<MatchDto> getUserMatches(String username) {
//        Integer userId = getUserIdByUsername(username);
//        List<Match> matches = matchRepository.findAllByUser(userId);
//        return matches.stream().map(match -> matchMapper.convertToMatchDto(match, userId)).collect(Collectors.toList());
        return fetchMatchesFromDatabase(username);
    }
    public void unMatch(String matchKey){
        User user = getUserFromAuth();
        Match match = matchRepository.findMatchByKey(matchKey, user.getId())
                .orElseThrow(() -> new MatchNotFoundException("No match found with key"));
        Match matched = matchRepository.findMatchByKey(matchKey, match.getMatchedUser().getId())
                .orElseThrow(() -> new MatchNotFoundException("No match found with key"));
        matchRepository.delete(match);
        matchRepository.delete(matched);
        updateMatchesCache(user.getUsername());
        updateMatchesCache(match.getMatchedUser().getUsername());
    }
    @CachePut(value = "matchesCache", key = "#username")
    public List<MatchDto> updateMatchesCache(String username){
        return fetchMatchesFromDatabase(username);
    }
    private List<MatchDto> fetchMatchesFromDatabase(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("No user found"));
        List<Match> matches = matchRepository.findAllByUser(user.getId());
        return matches.stream()
                .map(match -> matchMapper.convertToMatchDto(match, user.getId()))
                .collect(Collectors.toList());
    }
    private Integer getUserIdByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("No user found with username " + username));
        return user.getId();
    }
    private Message getLastMessage(String matchKey){
        return messageRepository.findLatestMessage(matchKey)
                .orElseThrow(() -> new MessageException("No latest message found"));
    }
    private User getUserFromAuth(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("No user found"));
    }
//    private MatchDto convertToMatchDto(Match match, Integer userId) {
//        MatchDto matchDto = new MatchDto();
//        matchDto.setMatchKey(match.getKey());
//        matchDto.setMatchedUserId(match.getMatchedUser().getId());
//        matchDto.setUserId(userId);
//        matchDto.setFirstname(match.getMatchedUser().getFirstName());
//        matchDto.setImage(getFirstImageUrl(match.getMatchedUser()));
//
//        messageRepository.findLatestMessage(match.getKey())
//                .map(message -> convertToMessageConvoDto(message, userId))
//                .ifPresent(matchDto::setMessage);
//        return matchDto;
//    }



    //        Integer userId = getUserIdByUsername(username);
//        List<Match> matches = matchRepository.findAllByUser(userId);
//        List<MatchDto> matchDtos = new ArrayList<>();
//        for (Match match : matches) {
//            MatchDto matchDto = new MatchDto();
//            matchDto.setMatchKey(match.getKey());
//            if (messageRepository.existsByMatchKey(match.getKey())) {
//                Message lastMessage = getLastMessage(match.getKey());
//                MessageConvoDto messageConvoDto = new MessageConvoDto();
//                if (lastMessage.getSenderId().equals(userId)) {
//                    messageConvoDto.setMethod("sent");
//                } else {
//                    messageConvoDto.setMethod("received");
//                }
//                messageConvoDto.setContent(lastMessage.getContent());
//                messageConvoDto.setTimestamp(lastMessage.getTimestamp());
//                messageConvoDto.setRead(lastMessage.isRead());
//                messageConvoDto.setMessageId(lastMessage.getId());
//                matchDto.setMessage(messageConvoDto);
//            }
//            matchDto.setMatchedUserId(match.getMatchedUser().getId());
//            List<Image> images = match.getMatchedUser().getProfile().getImages();
//            String image = images.get(0).getImageUrl();
//            matchDto.setImage(image);
//            matchDto.setUserId(match.getUser().getId());
//            matchDto.setFirstname(match.getMatchedUser().getFirstName());
//            matchDtos.add(matchDto);
//        }
//        return matchDtos;
}
