package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.rating.RatingDto;
import com.evanw.datebyrate.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService){
        this.ratingService = ratingService;
    }

    @PostMapping("/rate-user")
    @ResponseStatus(HttpStatus.OK)
    public void rateUser(@RequestBody RatingDto ratingDto){
        ratingService.rateUser(ratingDto);
    }
}
