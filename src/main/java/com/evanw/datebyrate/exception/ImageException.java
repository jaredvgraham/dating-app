package com.evanw.datebyrate.exception;


public class ImageException extends RuntimeException{

    private String msg;

    public ImageException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
