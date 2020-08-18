package com.trainchatapp.model;


public class Message {

    private String sender;
    private String receiver;
    private String text;
    private long timeSent;
    private long timeRead;

    public Message() {

    }

    public Message(String sender, String receiver, String text, long timeSent, long timeRead) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.timeSent = timeSent;
        this.timeRead = timeRead;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }

    public long getTimeRead() {
        return timeRead;
    }

    public void setTimeRead(long timeRead) {
        this.timeRead = timeRead;
    }
}
