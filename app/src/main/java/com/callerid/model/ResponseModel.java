package com.callerid.model;

import java.util.ArrayList;
import java.util.List;

public class ResponseModel {
    private float statusCode;
    private boolean status;
    Data data;


    // Getter Methods

    public float getStatusCode() {
        return statusCode;
    }

    public boolean getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    // Setter Methods

    public void setStatusCode(float statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String _id;
        private String integrationLeadId = null;
        private String name;
        private String phone;
        private String email = null;
        ArrayList<String> label = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        ExtraDetails extraDetails;
        private boolean isDistributed;
        private String saleValue = null;
        Address address;
        private String companyName = null;
        private String website = null;
        private String whatsApp;
        private String notes = null;
        private String customSource = null;
        ArrayList<Object> files = new ArrayList<Object>();
        ArrayList<Object> customField = new ArrayList<Object>();
        private String createdBy;
        private String organization;
        private String team = null;
        private String createdAt;
        private String recentTextNote;
        private String lastCallSince;
        private String resentTextNote;
        public UserPreference userPreference;
        private String integration;


        // Getter Methods


        public void setIntegration(String integration) {
            this.integration = integration;
        }

        public String getIntegration() {
            return integration;
        }

        public ArrayList<String> getStatus() {
            return status;
        }

        public void setStatus(ArrayList<String> status) {
            this.status = status;
        }

        public ArrayList<String> getLabel() {
            return label;
        }

        public void setLabel(ArrayList<String> label) {
            this.label = label;
        }

        public String get_id() {
            return _id;
        }

        public String getIntegrationLeadId() {
            return integrationLeadId;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public ExtraDetails getExtraDetails() {
            return extraDetails;
        }

        public boolean getIsDistributed() {
            return isDistributed;
        }

        public String getSaleValue() {
            return saleValue;
        }

        public Address getAddress() {
            return address;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getWebsite() {
            return website;
        }

        public String getWhatsApp() {
            return whatsApp;
        }

        public String getNotes() {
            return notes;
        }

        public String getCustomSource() {
            return customSource;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public String getTeam() {
            return team;
        }
        public String getLastCallSince() {
            return lastCallSince;
        }

        public String getResentTextNote() {
            return resentTextNote;
        }

        public String getRecentTextNote() {
            return recentTextNote;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public UserPreference getUserPreference() {
            return userPreference;
        }

        // Setter Methods

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setIntegrationLeadId(String integrationLeadId) {
            this.integrationLeadId = integrationLeadId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setExtraDetails(ExtraDetails extraDetails) {
            this.extraDetails = extraDetails;
        }

        public void setIsDistributed(boolean isDistributed) {
            this.isDistributed = isDistributed;
        }

        public void setSaleValue(String saleValue) {
            this.saleValue = saleValue;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public void setWhatsApp(String whatsApp) {
            this.whatsApp = whatsApp;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public void setCustomSource(String customSource) {
            this.customSource = customSource;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public void setTeam(String team) {
            this.team = team;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public void setUserPreference(UserPreference userPreferenceObject) {
            this.userPreference = userPreference;
        }
    }

    public class UserPreference {
        public List<StatusModel> status;
        public List<LabelModel> labels;

        public List<StatusModel> getUser_status() {
            return status;
        }

        public void setUser_status(List<StatusModel> user_status) {
            this.status = user_status;
        }

        public List<LabelModel> getUser_Label() {
            return labels;
        }

        public void setUser_Label(List<LabelModel> labels) {
            this.labels = labels;
        }
        // Getter Methods


        // Setter Methods


    }

    public class Address {


        // Getter Methods


        // Setter Methods


    }

    public class ExtraDetails {

        private String sale_value;

        public String getSale_value() {
            return sale_value;
        }

        public void setSale_value(String sale_value) {
            this.sale_value = sale_value;
        }
        // Getter Methods


        // Setter Methods


    }


}