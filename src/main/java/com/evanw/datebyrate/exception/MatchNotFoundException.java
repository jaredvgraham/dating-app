package com.evanw.datebyrate.exception;

public class MatchNotFoundException extends RuntimeException{
    private String msg;

    public MatchNotFoundException(
            String msg
    ) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
