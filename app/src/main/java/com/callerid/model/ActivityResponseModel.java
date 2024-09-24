package com.callerid.model;

import java.util.ArrayList;

public class ActivityResponseModel {
    private boolean status;
    private String message;
    private int statusCode;
    private ArrayList<ActivityModel> data;

    public boolean isSuccess() {
        return status;
    }

    public void setSuccess(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return statusCode;
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<ActivityModel> getData() {
        return data;
    }

    public void setData(ArrayList<ActivityModel> data) {
        this.data = data;
    }
}