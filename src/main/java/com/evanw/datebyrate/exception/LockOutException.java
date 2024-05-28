package com.evanw.datebyrate.exception;

public class LockOutException extends RuntimeException{

    private String msg;

    public LockOutException(String msg){
        this.msg = msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}
