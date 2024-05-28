package com.evanw.datebyrate.exception;

public class MessageException extends RuntimeException{

    private String msg;

    public MessageException(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
