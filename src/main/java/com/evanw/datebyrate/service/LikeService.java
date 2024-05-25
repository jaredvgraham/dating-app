package com.evanw.datebyrate.service;

import com.evanw.datebyrate.Dto.like.LikeResponseDto;
import com.evanw.datebyrate.Dto.match.MatchDto;
import com.evanw.datebyrate.controller.NotificationController;
import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.models.Like;
import com.evanw.datebyrate.models.Match;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.LikeRepository;
import com.evanw.datebyrate.repository.MatchRepository;
import com.evanw.datebyrate.repository.MessageRepository;
import com.evanw.datebyrate.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LikeService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final MatchRepository matchRepository;
    private final NotificationController notificationController;
    private final MatchService matchService;

    public LikeService(UserRepository userRepository,
                       LikeRepository likeRepository,
                       MatchRepository matchRepository,
                       NotificationController notificationController,
                       MatchService matchService) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.matchRepository = matchRepository;
        this.notificationController = notificationController;
        this.matchService = matchService;
    }

    @Transactional
    public LikeResponseDto likeUser(Integer likedId) {
        Like like = new Like();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User liker = getUserByUsername(username);
        User liked = getUserById(likedId);
        like.setLiked(liked);
        like.setLiker(liker);
        likeRepository.save(like);
        return checkForMatch(liked, liker);
    }
    private LikeResponseDto checkForMatch(User liked, User liker) {
        Optional<Like> likedBack = likeRepository.findByLikerAndLiked(liked, liker);
        if (likedBack.isPresent()) {
            String matchKey = String.valueOf(UUID.randomUUID());
            Match matchLiker = createMatchForUser(liker, liked, matchKey);
            matchRepository.save(matchLiker);
            Match matchLiked = createMatchForUser(liked, liker, matchKey);
            matchRepository.save(matchLiked);

            matchService.updateMatchesCache(liker.getUsername());
            matchService.updateMatchesCache(liked.getUsername());

            System.out.println("Sending notification to: " + liked.getUsername() + " and: " + liker.getUsername());
            sendMatchNotificationToBothUsers(liked, liker, matchLiked, matchLiker);
            return new LikeResponseDto("Like successful. Match created!", HttpStatus.OK);
        } else {
            return new LikeResponseDto("Like successful. No match created.", HttpStatus.OK);
        }
    }
    private Match createMatchForUser(User user, User matchedUser, String matchKey){
        Match match = new Match();
        match.setMatchedUser(matchedUser);
        match.setUser(user);
        match.setMatchDate(LocalDateTime.now());
        match.setKey(matchKey);
        return match;
    }
    private void sendMatchNotificationToBothUsers(User liked, User liker, Match matchLiked, Match matchLiker) {
        try {
            notificationController.sendMatchNotification(liked.getId(), matchLiked);
            notificationController.sendMatchNotification(liker.getId(), matchLiker);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private User getUserByUsername(String username){
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()){
            throw new UserNotFoundException("No user found");
        }
        return userOpt.get();
    }
    private User getUserById(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()){
            throw new UserNotFoundException("No user found");
        }
        return userOpt.get();
    }
}
