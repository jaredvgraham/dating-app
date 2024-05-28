package com.evanw.datebyrate.Dto;

public class LoginResponse {

    private Integer userId;
    private String accessToken;

    public LoginResponse(Integer userId, String accessToken) {
        this.accessToken = accessToken;
        this.userId = userId;
    }
    public LoginResponse(){}

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
