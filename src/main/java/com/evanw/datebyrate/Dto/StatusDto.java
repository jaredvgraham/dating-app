package com.evanw.datebyrate.Dto;

import jdk.jshell.Snippet;
import org.springframework.http.HttpStatusCode;

public class StatusDto {

    private Integer userId;
    private boolean session;
    private String username;

    public StatusDto(
            Integer userId,
            boolean session,
            String username
    ){
        this.userId = userId;
        this.session = session;
        this.username = username;
    }

    public boolean isSession() {
        return session;
    }

    public void setSession(boolean session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
