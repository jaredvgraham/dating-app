package com.evanw.datebyrate.Dto;

public class AccountCreationResponseDto {

    private String message;

    public AccountCreationResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage() {
        this.message = message;
    }
}
