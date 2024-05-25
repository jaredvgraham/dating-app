package com.evanw.datebyrate.Dto;


public class RegistrationDto {
    private String message;
//    private Integer id;

    public RegistrationDto(String message) {
        this.message = message;
//        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
