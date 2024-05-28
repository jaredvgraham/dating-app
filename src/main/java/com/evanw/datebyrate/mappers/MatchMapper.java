package com.evanw.datebyrate.mappers;

import com.evanw.datebyrate.Dto.match.MatchDto;
import com.evanw.datebyrate.Dto.message.MessageConvoDto;
import com.evanw.datebyrate.exception.ImageException;
import com.evanw.datebyrate.models.Image;
import com.evanw.datebyrate.models.Match;
import com.evanw.datebyrate.models.Message;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchMapper {

    private MessageRepository messageRepository;

    public MatchMapper(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public MatchDto convertToMatchDto(Match match, Integer userId) {
        MatchDto matchDto = new MatchDto();
        matchDto.setMatchKey(match.getKey());
        matchDto.setMatchedUserId(match.getMatchedUser().getId());
        matchDto.setUserId(userId);
        matchDto.setFirstname(match.getMatchedUser().getFirstName());
        matchDto.setImage(getFirstImageUrl(match.getMatchedUser()));

        messageRepository.findLatestMessage(match.getKey())
                .map(message -> convertToMessageConvoDto(message, userId))
                .ifPresent(matchDto::setMessage);
        return matchDto;
    }
    private String getFirstImageUrl(User user) {
        return user.getProfile().getImages().stream()
                .findFirst()
                .map(Image::getImageUrl)
                .orElseThrow(() -> new ImageException("No image found"));
    }
    private MessageConvoDto convertToMessageConvoDto(Message message, Integer userId) {
        MessageConvoDto messageConvoDto = new MessageConvoDto();
        messageConvoDto.setMethod(message.getSenderId().equals(userId) ? "sent" : "received");
        messageConvoDto.setContent(message.getContent());
        messageConvoDto.setTimestamp(message.getTimestamp());
        messageConvoDto.setRead(message.isRead());
        messageConvoDto.setMessageId(message.getId());
        return messageConvoDto;
    }
}
