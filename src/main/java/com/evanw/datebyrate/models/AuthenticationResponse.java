package com.evanw.datebyrate.models;

public class AuthenticationResponse {

    private Integer userId;
    private String username;
    private String accessToken;
    private String refreshToken;

    public AuthenticationResponse(
            Integer userId,
            String accessToken,
            String refreshToken,
            String username
    ) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
    }
    public AuthenticationResponse(Integer userId, String accessToken){
        this.userId = userId;
        this.accessToken = accessToken;
    }

    public AuthenticationResponse() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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
