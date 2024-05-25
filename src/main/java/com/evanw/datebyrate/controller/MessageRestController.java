package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.message.MessageDto;
import com.evanw.datebyrate.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageRestController {

    private MessageService messageService;

    public MessageRestController(MessageService messageService) {
        this.messageService = messageService;
    }
    @GetMapping("/conversation/{matchKey}")
    public List<MessageDto> getMessages(@PathVariable String matchKey) {
        return messageService.getMessagesByMatchKey(matchKey);
    }
    @PostMapping("/read-message/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    public void readMessage(@PathVariable("messageId") Integer messageId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        messageService.readMessage(messageId, auth);
    }
}
