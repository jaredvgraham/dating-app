package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.profile.ProfileUserInfoDisplayDto;
import com.evanw.datebyrate.service.UserService;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedController {
    private final UserService userService;

    public FeedController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile-feed")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProfileUserInfoDisplayDto>> findNearbyProfiles() throws ParseException {
        List<ProfileUserInfoDisplayDto> list = userService.findNearbyUsers();
        return ResponseEntity.ok(list);
    }
}
