package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.user.UserResponseDto;
import com.evanw.datebyrate.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> findAllUsers(){
        return userService.findAllUsers();
    }
}
