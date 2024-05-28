package com.evanw.datebyrate.exception;

public class AgeException extends RuntimeException{
    private String msg;

    public AgeException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
