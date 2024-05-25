package com.evanw.datebyrate.models;

import com.evanw.datebyrate.Dto.match.MatchDto;
import com.evanw.datebyrate.controller.NotificationController;

public class NotificationMessage {

    private String msg;
    private MatchDto matchedUser;

    public NotificationMessage(String msg, MatchDto matchedUser) {
        this.msg = msg;
        this.matchedUser = matchedUser;
    }

    public NotificationMessage() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MatchDto getMatchedUser() {
        return matchedUser;
    }

    public void setMatchedUser(MatchDto matchedUser) {
        this.matchedUser = matchedUser;
    }
}
