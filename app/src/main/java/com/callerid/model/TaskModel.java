package com.callerid.model;

import com.callerid.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaskModel {


        private String _id;

        private Integer user_id;

        private Integer lead_id;

        private String type;

        private String toBePerformAt;

        private ExtraModel extra_details;

        private Integer deleted;

        private String created_at;

        private String updated_at;

        private String assigned_to;

        private String leadname;


        private String createdBy;

        private String assigned_to_name;

        private String createdAt;


       private ArrayList<Lead> lead;


    public String getCreatedAt() {
        return createdAt;
    }

    public ArrayList<Lead> getLead() {
        return lead;
    }

    public String getId() {
            return _id;
        }

        public void setId(String _id) {
            this._id = _id;
        }

        public Integer getUser_id() {
            return user_id;
        }

        public void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        public Integer getLead_id() {
            return lead_id;
        }

        public void setLead_id(Integer lead_id) {
            this.lead_id = lead_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTo_be_performed_at() {
            return toBePerformAt;
        }

        public void setTo_be_performed_at(String toBePerformAt) {
            this.toBePerformAt = toBePerformAt;
        }

        public ExtraModel getExtra_details() {
            return extra_details;
        }

        public void setExtra_details(ExtraModel extra_details) {
            this.extra_details = extra_details;
        }

        public Integer getDeleted() {
            return deleted;
        }

        public void setDeleted(Integer deleted) {
            this.deleted = deleted;
        }

        public String getCreated_at() {
            return createdBy;
        }

        public void setCreated_at(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getAssigned_to() {
            return assigned_to;
        }

        public void setAssigned_to(String  assigned_to) {
            this.assigned_to = assigned_to;
        }

        public String getLeadname() {
            return leadname;
        }

        public void setLeadname(String leadname) {
            this.leadname = leadname;
        }

        public String getCreated_by_name() {
            return createdBy;
        }

        public void setCreated_by_name(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getAssigned_to_name() {
            return assigned_to_name;
        }

        public void setAssigned_to_name(String assigned_to_name) {
            this.assigned_to_name = assigned_to_name;
        }

    /** Getting value with key From JSON Object*/
    public String getSafeValue(JSONObject object, String key){
        String result = "";
        try {
            if(object.has(key)){
                result = object.getString(key);
                if(Utils.isInvalidString(result))
                    result = "";
            }
            else{
                result = "";
            }
        } catch (JSONException e) {

            e.printStackTrace();
            return result;
        }
        return result;
    }
}
