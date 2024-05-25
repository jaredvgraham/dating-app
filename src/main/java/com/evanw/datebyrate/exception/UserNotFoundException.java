package com.evanw.datebyrate.exception;

public class UserNotFoundException extends RuntimeException{

    private String msg;

    public UserNotFoundException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
