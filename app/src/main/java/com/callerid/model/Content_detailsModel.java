package com.callerid.model;

import java.util.List;

public class Content_detailsModel {


        private String title;

        private Object coords;

        private List<String> imageUrls = null;

        private String videoUrls;

        private String description;

        private List<Tags> tag = null;

        String message;
        FileModel file;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getCoords() {
            return coords;
        }

        public void setCoords(Object coords) {
            this.coords = coords;
        }

        public List<String> getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }

        public String getVideoUrls() {
            return videoUrls;
        }

        public void setVideoUrls(String videoUrls) {
            this.videoUrls = videoUrls;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Tags> getSelectedTags() {
            return tag;
        }

        public void setSelectedTags(List<Tags> tag) {
            this.tag = tag;
        }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
    }

    public class Tags {
        private String tag;

        public String getTag() {
            return tag;
        }
    }
}

