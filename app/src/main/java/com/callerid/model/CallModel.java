package com.callerid.model;

public class CallModel {
    private String name, number, date, duration, callType;

    public CallModel(String number, String callType) {
        this.number = number;
        this.callType = callType;
    }
    public CallModel(String number, String callType, String duration) {
        this.number = number;
        this.callType = callType;
    }
    public CallModel(String name, String number, String date, String duration, String callType) {
        this.name = name;
        this.number = number;
        this.date = date;
        this.duration = duration;
        this.callType = callType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}