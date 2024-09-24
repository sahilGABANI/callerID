package com.callerid.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class ObjectResponseModel {
    private boolean success;
    private String message;
    private int status;
    private ContentModel data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ContentModel getData() {
        return data;
    }

    public void setData(ContentModel data) {
        this.data = data;
    }
}