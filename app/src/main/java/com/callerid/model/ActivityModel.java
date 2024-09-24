package com.callerid.model;

public class ActivityModel {

        private String _id;
       Integer user_id;

        private Integer lead_id;

        private String type;

    private String note;

        private String performedAt;

        private ExtraModel extraDetails;

        private Integer deleted;

        private String createdAt;

        private String updated_at;

        private String performed_by;

        private String created_timestamp;

        private String leadname;

        private ContentModel content;

        private String createdBy;

        public String getId() {
            return _id;
        }

        public void setId(String id) {
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

        public String getPerformed_at() {
            return performedAt;
        }

        public void setPerformed_at(String performedAt) {
            this.performedAt = performedAt;
        }



        public Integer getDeleted() {
            return deleted;
        }

        public void setDeleted(Integer deleted) {
            this.deleted = deleted;
        }

        public String getCreated_at() {
            return createdAt;
        }

        public void setCreated_at(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getPerformed_by() {
            return performed_by;
        }

        public void setPerformed_by(String performed_by) {
            this.performed_by = performed_by;
        }

    public String getNote() {
        return note;
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

    public ExtraModel getExtra_details() {
        return extraDetails;
    }

    public void setExtra_details(ExtraModel extraDetails) {
        this.extraDetails = extraDetails;
    }

    public String getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(String created_timestamp) {
        this.created_timestamp = created_timestamp;
    }

    public ContentModel getContent() {
        return content;
    }

    public void setContent(ContentModel content) {
        this.content = content;
    }
}
