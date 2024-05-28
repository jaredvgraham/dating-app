package com.evanw.datebyrate.exception;

public class InvalidTokenException extends RuntimeException{
    private String msg;

    public InvalidTokenException(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
