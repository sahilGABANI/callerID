package com.callerid.model;

import org.json.JSONArray;

public class SimpleResponseModel {
    private boolean status;
    private String message;
    private int statusCode;


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

}