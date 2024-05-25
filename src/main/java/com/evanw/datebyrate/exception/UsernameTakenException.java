package com.evanw.datebyrate.exception;

public class UsernameTakenException extends RuntimeException{
    private String msg;
    public UsernameTakenException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
