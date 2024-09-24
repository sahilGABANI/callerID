package com.callerid.model;

import com.callerid.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ContentModel {

        String uniqueLink;
        private String _id;

        private Integer user_id;

        private String type;

        private Content_detailsModel details;

        private Integer deleted;

        private String createdAt;
        private  String createdBy;
        private Object viewcount;

    public String getCreatedBy() {
        return createdBy;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Content_detailsModel getContent_details() {
            return details;
        }

        public void setContent_details(Content_detailsModel content_details) {
            this.details = details;
        }

    public String getUniqueLink() {
        return uniqueLink;
    }

    public void setUniqueLink(String uniqueLink) {
        this.uniqueLink = uniqueLink;
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



        public Object getViewcount() {
            return viewcount;
        }

        public void setViewcount(Object viewcount) {
            this.viewcount = viewcount;
        }
}
