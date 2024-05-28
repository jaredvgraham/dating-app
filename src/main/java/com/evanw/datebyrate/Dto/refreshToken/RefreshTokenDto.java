package com.evanw.datebyrate.Dto.refreshToken;

import java.time.Instant;

public class RefreshTokenDto {
    private String accessToken;
    private String refreshToken;
    private Instant refreshTokenExpireAt;

    public RefreshTokenDto(String accessToken,
                           String refreshToken,
                           Instant refreshTokenExpireAt
    ){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpireAt = refreshTokenExpireAt;
    }
    public RefreshTokenDto() {
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

    public Instant getRefreshTokenExpireAt() {
        return refreshTokenExpireAt;
    }

    public void setRefreshTokenExpireAt(Instant refreshTokenExpireAt) {
        this.refreshTokenExpireAt = refreshTokenExpireAt;
    }
}
