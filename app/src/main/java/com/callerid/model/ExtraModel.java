package com.callerid.model;

public class ExtraModel {
    private String dob, gender, alternate_phone;
    boolean isComplete;
    String description;
    String next_status;
    String type;
    String sale_value;
    String notes;
    String task_assigned_to_name;
    String next_label;
    String list_name, transfer_by_name, transfer_to_name;
    String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTask_assigned_to_name() {
        return task_assigned_to_name;
    }

    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    public String getTransfer_by_name() {
        return transfer_by_name;
    }

    public void setTransfer_by_name(String transfer_by_name) {
        this.transfer_by_name = transfer_by_name;
    }

    public String getTransfer_to_name() {
        return transfer_to_name;
    }

    public void setTransfer_to_name(String transfer_to_name) {
        this.transfer_to_name = transfer_to_name;
    }

    public void setTask_assigned_to_name(String task_assigned_to_name) {
        this.task_assigned_to_name = task_assigned_to_name;
    }

    public String getNext_label() {
        return next_label;
    }

    public void setNext_label(String next_label) {
        this.next_label = next_label;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNext_status() {
        return next_status;
    }

    public void setNext_status(String next_status) {
        this.next_status = next_status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDob() {
        return dob;
    }

    public String getSale_value() {
        return sale_value;
    }

    public void setSale_value(String sale_value) {
        this.sale_value = sale_value;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAlternate_phone() {
        return alternate_phone;
    }

    public void setAlternate_phone(String alternate_phone) {
        this.alternate_phone = alternate_phone;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}