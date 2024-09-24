package com.callerid.model;

import java.util.ArrayList;

public class LinkRequestModel {

    ArrayList<String> lead_ids = new ArrayList<>();

        String content_type;
        String performed_at;
        String content_id;
        String performed_by;

    public LinkRequestModel() {

    }

    public ArrayList<String> getLead_ids() {
        return lead_ids;
    }

    public void setLead_ids(ArrayList<String> lead_ids) {
        this.lead_ids = lead_ids;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getPerformed_at() {
        return performed_at;
    }

    public void setPerformed_at(String performed_at) {
        this.performed_at = performed_at;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getPerformed_by() {
        return performed_by;
    }

    public void setPerformed_by(String performed_by) {
        this.performed_by = performed_by;
    }
}
