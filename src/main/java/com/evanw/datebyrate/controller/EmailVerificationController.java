package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class EmailVerificationController {

    private UserRepository userRepository;

    public EmailVerificationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @GetMapping("/verify-email")
//    public String verifyEmail(@RequestParam("token") String token) {
//        Optional<User> user = userRepository.findByVerificationToken(token);
//
//        if (user != null) {
//            user.get().setEmailVerified(true);
//            userRepository.save(user.get());
//            return "Email Verified Successfully";
//        } else {
//            return "Invalid or expired token";
//        }
//    }
}
