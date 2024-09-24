package com.callerid.model;

import java.util.ArrayList;

public class RequestModel {
    int user_id, lead_id;
    String id;
    String number;
    ArrayList<String> lead_ids = new ArrayList<>();
  String type;
       String to_be_performed_at;
       ExtraModel extra_details;
        int employee_id;

    public RequestModel(int user, int lead, String id, String number) {
        this.setUser_id(user);
        this.setLead_id(lead);
        this.setId(id);
        this.setNumber(number);


    }
    public RequestModel() {

    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public ArrayList<String> getLead_ids() {
        return lead_ids;
    }

    public void setLead_ids(ArrayList<String> lead_ids) {
        this.lead_ids = lead_ids;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getLead_id() {
        return lead_id;
    }

    public void setLead_id(int lead_id) {
        this.lead_id = lead_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo_be_performed_at() {
        return to_be_performed_at;
    }

    public void setTo_be_performed_at(String to_be_performed_at) {
        this.to_be_performed_at = to_be_performed_at;
    }

    public ExtraModel getExtra_details() {
        return extra_details;
    }

    public void setExtra_details(ExtraModel extra_details) {
        this.extra_details = extra_details;
    }
}
