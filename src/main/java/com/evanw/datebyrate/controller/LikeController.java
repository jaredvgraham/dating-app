package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.like.LikeResponseDto;
import com.evanw.datebyrate.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like/{liked-id}")
    @ResponseStatus
    public ResponseEntity<LikeResponseDto> likeUser(@PathVariable("liked-id") Integer likedId) {
        try {
            LikeResponseDto response = likeService.likeUser(likedId);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LikeResponseDto(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LikeResponseDto("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
