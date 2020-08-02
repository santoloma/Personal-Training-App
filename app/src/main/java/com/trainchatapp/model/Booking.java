package com.trainchatapp.model;

public class Booking {

    private String trainerId;
    private String clientId;
    private long timeBooked;
    private String date;
    private String timeFrom;
    private String duration;
    private boolean accepted;
    private long timeCancelled;
    private String notes;
    private boolean declined;
    private String location;


    public Booking() {
    }

    public Booking(String trainerId, String clientId, long timeBooked,
                   String date, String timeFrom, String duration,
                   boolean accepted, long timeCancelled, String notes,
                   boolean declined, String location) {
        this.trainerId = trainerId;
        this.clientId = clientId;
        this.timeBooked = timeBooked;
        this.date = date;
        this.timeFrom = timeFrom;
        this.duration = duration;
        this.accepted = accepted;
        this.timeCancelled = timeCancelled;
        this.notes = notes;
        this.declined = declined;
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getTimeCancelled() {
        return timeCancelled;
    }

    public void setTimeCancelled(long timeCancelled) {
        this.timeCancelled = timeCancelled;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public long getTimeBooked() {
        return timeBooked;
    }

    public void setTimeBooked(long timeBooked) {
        this.timeBooked = timeBooked;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isDeclined() {
        return declined;
    }

    public void setDeclined(boolean declined) {
        this.declined = declined;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}