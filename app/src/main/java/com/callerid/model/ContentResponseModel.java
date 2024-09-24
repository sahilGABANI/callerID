package com.callerid.model;

import java.util.ArrayList;

public class ContentResponseModel {
    private boolean status;
    private String message;
    private int statusCode;
    private int page;
    private int total;
    private ArrayList<ContentModel> data;


    public int getPage() {
        return page;
    }

    public int getTotal() {
        return total;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ContentModel> getData() {
        return data;
    }

    public void setData(ArrayList<ContentModel> data) {
        this.data = data;
    }
}