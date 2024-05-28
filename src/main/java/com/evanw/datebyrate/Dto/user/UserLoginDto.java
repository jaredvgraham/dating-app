package com.evanw.datebyrate.Dto.user;

public record UserLoginDto(
        String username,
        String password,
        Location location
) {
    public static record Location(double latitude, double longitude){
    }
}
