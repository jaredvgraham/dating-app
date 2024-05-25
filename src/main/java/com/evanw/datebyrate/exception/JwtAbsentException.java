package com.evanw.datebyrate.exception;

public class JwtAbsentException extends RuntimeException{
    private String msg;

    public JwtAbsentException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
