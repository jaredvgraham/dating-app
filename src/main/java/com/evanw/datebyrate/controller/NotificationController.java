package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.match.MatchDto;
import com.evanw.datebyrate.exception.MessageException;
import com.evanw.datebyrate.mappers.MatchMapper;
import com.evanw.datebyrate.models.Match;
import com.evanw.datebyrate.models.NotificationMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MatchMapper matchMapper;

    public NotificationController(SimpMessagingTemplate simpMessagingTemplate, MatchMapper matchMapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.matchMapper = matchMapper;
    }
    public void sendMatchNotification(Integer userId, Match match) {
        if (userId == null || match.getMatchedUser() == null) {
            System.out.println("Invalid userId: " + userId + " or invalid match obj " + match.getMatchedUser().getUsername());
            return;
        }
        MatchDto matchResp = matchMapper.convertToMatchDto(match, match.getUser().getId());
        NotificationMessage notification = new NotificationMessage("New match created! ", matchResp);
        String destination = "/topic/notifications/" + userId;
        System.out.println("Sending notification to destination: " + destination);
        try {
            simpMessagingTemplate.convertAndSend(destination, notification);
            System.out.println(notification.getMatchedUser().getMessage() + " " + notification.getMatchedUser().getMatchedUserId() + " "  + notification.getMatchedUser().getImage() + " " + notification.getMsg());
            System.out.println("message sent successfully!");
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
