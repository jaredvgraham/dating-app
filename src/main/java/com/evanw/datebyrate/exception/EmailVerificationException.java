package com.evanw.datebyrate.exception;

public class EmailVerificationException extends RuntimeException{

    private String msg;

    public EmailVerificationException(String msg) {
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
