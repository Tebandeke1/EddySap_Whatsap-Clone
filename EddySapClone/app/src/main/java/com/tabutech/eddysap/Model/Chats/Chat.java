package com.tabutech.eddysap.Model.Chats;

public class Chat {
    private String dateTime;
    private String textMessage;
    private String uri;
    private String type;
    private String sender;
    private String receiver;

    public Chat() {

    }

    public Chat(String dateTime, String textMessage,String uri, String type, String sender, String receiver) {
        this.dateTime = dateTime;
        this.textMessage = textMessage;
        this.type = type;
        this.uri = uri;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
