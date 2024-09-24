package com.callerid.model;

import com.callerid.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class NoteModel {
    private String _id;
    private String createdBy;
    private String description;
    private String createdAt;
    public Lead lead;


    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }


    public NoteModel() {

    }

}
