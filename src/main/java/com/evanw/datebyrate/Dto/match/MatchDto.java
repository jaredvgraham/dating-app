package com.evanw.datebyrate.Dto.match;

import com.evanw.datebyrate.Dto.message.MessageConvoDto;
import com.evanw.datebyrate.models.Image;
import com.evanw.datebyrate.models.Profile;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class MatchDto implements Serializable {

    @JsonInclude
    private String matchKey;
    private Integer matchedUserId;

    private Integer userId;
    private String imageUrl;

    private String firstname;

    private MessageConvoDto message;

    public MatchDto(
            Integer matchedUserId,
            Integer userId,
            String matchKey,
            String imageUrl,
            String firstname,
            MessageConvoDto message
    ) {
        this.matchedUserId = matchedUserId;
        this.imageUrl = imageUrl;
        this.firstname = firstname;
        this.matchKey = matchKey;
        this.message = message;
        this.userId = userId;
    }

    public MatchDto() {
    }

    public Integer getMatchedUserId(){
        return this.matchedUserId;
    }
    public void setMatchedUserId(Integer matchedUserId) {
        this.matchedUserId = matchedUserId;
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMatchKey() {
        return matchKey;
    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public MessageConvoDto getMessage() {
        return message;
    }

    public void setMessage(MessageConvoDto message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
