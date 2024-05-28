package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.message.MessageDto;
import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.models.Match;
import com.evanw.datebyrate.models.Message;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.UserRepository;
import com.evanw.datebyrate.service.MessageService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class MessageController {

    private MessageService messageService;
    private UserRepository userRepository;

    public MessageController(
            MessageService messageService,
            UserRepository userRepository
    ) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }
    @MessageMapping("/chat/{matchKey}")
    @SendTo("/topic/chat/{matchKey}")
    public MessageDto sendMessage(
            @DestinationVariable String matchKey,
            @Payload Message message// Change the payload type to String
    ) {
        String username = message.getUsername();
        String content = message.getContent();

        if (username == null) {
            throw new UserNotFoundException("Username not found in session");
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOpt.get();
        Integer senderId = user.getId();
        return messageService.save(content, senderId, matchKey);
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/public")
    public Message newUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", message.getUsername());
        return message;
    }




//    private User getCurrentUser() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        System.out.println(auth);
//        Optional<User> userOpt = userRepository.findByUsername(username);
//        if (userOpt.isEmpty()) {
//            throw new UserNotFoundException("No user found");
//        }
//        return userOpt.get();
//    }
}
