package com.evanw.datebyrate.exception;

public class NearbyUserException extends RuntimeException{
    private String msg;

    public NearbyUserException(String msg) {
        this.msg = msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}
