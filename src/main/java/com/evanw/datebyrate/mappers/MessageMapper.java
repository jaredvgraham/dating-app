package com.evanw.datebyrate.mappers;

import com.evanw.datebyrate.Dto.match.MatchDto;
import com.evanw.datebyrate.Dto.message.MessageDto;
import com.evanw.datebyrate.models.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {

    public MessageDto toMessageDtoFromMessage(Message message){
        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getTimestamp(),
                message.getSenderId(),
                message.getReceiverId(),
                message.isRead()
        );
    }
}
