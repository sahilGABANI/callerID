package com.callerid.model;

import java.util.ArrayList;

public class NoteResponseModel {
    private boolean status;
    private String message;
    private int statusCode;
    private int page;
    private int total;
    private ArrayList<NoteModel> data;


    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public ArrayList<NoteModel> getData() {
        return data;
    }

    public void setData(ArrayList<NoteModel> data) {
        this.data = data;
    }
}