package com.callerid.model;

import java.util.ArrayList;

public class TaskResponseModel {
    private boolean status;
    private String message;
    private int statusCode;
    private ArrayList<TaskModel> data;
    private int page;
    private int total;


    public int getPage() {
        return page;
    }

    public int getTotal() {
        return total;
    }

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

    public ArrayList<TaskModel> getData() {
        return data;
    }

    public void setData(ArrayList<TaskModel> data) {
        this.data = data;
    }
}