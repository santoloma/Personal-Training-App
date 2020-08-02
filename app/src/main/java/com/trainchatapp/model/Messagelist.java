package com.trainchatapp.model;

public class Messagelist {

    private String id;
    private long lastMsgTime;

    public Messagelist() {
    }

    public Messagelist(String id, long lastMsgTime) {
        this.id = id;
        this.lastMsgTime = lastMsgTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public long getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(long lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }
}
