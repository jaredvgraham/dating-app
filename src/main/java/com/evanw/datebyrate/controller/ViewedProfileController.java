package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.UserRepository;
import com.evanw.datebyrate.service.ViewedProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ViewedProfileController {

    private UserRepository userRepository;
    private ViewedProfileService viewedProfileService;

    public ViewedProfileController(
            UserRepository userRepository,
            ViewedProfileService viewedProfileService
    ) {
        this.userRepository = userRepository;
        this.viewedProfileService = viewedProfileService;
    }

    @PostMapping("/viewed/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void viewedUser(@PathVariable("id") Integer viewedId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No user found.");
        }
        User viewerId = user.get();
        viewedProfileService.viewedProfile(viewerId, viewedId);
    }
}
