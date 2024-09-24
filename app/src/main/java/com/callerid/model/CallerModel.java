package com.callerid.model;

import com.callerid.utils.Utils;

import java.util.List;

public class CallerModel {
    private int id, user_id;
    private String lead_id, name, status, created_at, integration_name;
    private PhoneModel primary_phone;
    private ExtraModel extra_details;
    private List<String> label;
    private List<LabelModel> user_labels;
    private List<StatusModel> user_status;

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLead_id() {
        if(Utils.isInvalidString(lead_id)) {
            return String.valueOf(id);
        }
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getIntegration_name() {
        return integration_name;
    }

    public void setIntegration_name(String integration_name) {
        this.integration_name = integration_name;
    }

    public PhoneModel getPrimary_phone() {
        return primary_phone;
    }

    public void setPrimary_phone(PhoneModel primary_phone) {
        this.primary_phone = primary_phone;
    }

    public ExtraModel getExtra_detaails() {
        return extra_details;
    }

    public void setExtra_detaails(ExtraModel extra_detaails) {
        this.extra_details = extra_detaails;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public List<LabelModel> getUser_labels() {
        return user_labels;
    }

    public void setUser_labels(List<LabelModel> user_labels) {
        this.user_labels = user_labels;
    }

    public List<StatusModel> getUser_status() {
        return user_status;
    }

    public void setUser_status(List<StatusModel> user_status) {
        this.user_status = user_status;
    }
}