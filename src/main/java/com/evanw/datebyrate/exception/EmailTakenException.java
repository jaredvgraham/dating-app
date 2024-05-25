package com.evanw.datebyrate.exception;

public class EmailTakenException extends RuntimeException{
    private String msg;

    public EmailTakenException(String msg) {
        this.msg = msg;
    }
    public EmailTakenException(){
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
