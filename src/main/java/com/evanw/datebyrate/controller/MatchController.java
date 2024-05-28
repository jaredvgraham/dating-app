package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.match.MatchDto;
import com.evanw.datebyrate.models.Match;
import com.evanw.datebyrate.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MatchController {

    private MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/matches")
    public List<MatchDto> getUserMatches(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return matchService.getUserMatches(username);
    }
    @DeleteMapping("/unmatch/{matchKey}")
    @ResponseStatus(HttpStatus.OK)
    public void unMatch(@PathVariable("matchKey") String matchKey){
        matchService.unMatch(matchKey);
    }
}
