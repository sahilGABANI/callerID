package com.callerid.model;

import java.util.ArrayList;

public class UploadImage {
    public int statusCode;
    public boolean status;
    public UploadImage.Datum data;

    public boolean isStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public UploadImage.Datum getData() {
        return data;
    }

    public class Datum{
        public String type;
        public String lead;
        public ArrayList<UploadImage.Files> files;


        public String getLead() {
            return lead;
        }

        public String getType() {
            return type;
        }

        public ArrayList<Files> getFiles() {
            return files;
        }
    }


    public class Files{
        public String filePath;
        public String fileName;


        public String getFileName() {
            return fileName;
        }

        public String getFilePath() {
            return filePath;
        }
    }

}
