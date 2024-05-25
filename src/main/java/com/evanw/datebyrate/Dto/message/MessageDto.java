package com.evanw.datebyrate.Dto.message;

import java.time.LocalDateTime;

public class MessageDto {

    private Integer messageId;

    private String message;

    private LocalDateTime timestamp;

    private Integer senderId;

    private Integer receiverId;

    private boolean read;


    public MessageDto(
            Integer messageId,
            String message,
            LocalDateTime timestamp,
            Integer senderId,
            Integer receiverId,
            boolean read
    ) {
        this.messageId = messageId;
        this.message = message;
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.read = read;
    }
    public MessageDto(){
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
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
