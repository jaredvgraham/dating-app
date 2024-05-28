package com.evanw.datebyrate.Dto.message;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageConvoDto implements Serializable {

    private Integer messageId;

    private String content;

    private LocalDateTime timestamp;

    private String method;

    private boolean read;

    public MessageConvoDto(String content,
                           Integer messageId,
                           LocalDateTime timestamp,
                           String method,
                           boolean read) {
        this.content = content;
        this.timestamp = timestamp;
        this.method = method;
        this.messageId = messageId;
        this.read = read;
    }
    public MessageConvoDto(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
