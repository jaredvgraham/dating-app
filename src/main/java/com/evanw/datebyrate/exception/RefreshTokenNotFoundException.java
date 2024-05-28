package com.evanw.datebyrate.exception;

public class RefreshTokenNotFoundException extends RuntimeException{
    private String msg;

    public RefreshTokenNotFoundException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
