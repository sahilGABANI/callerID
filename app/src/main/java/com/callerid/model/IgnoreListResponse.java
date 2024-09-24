package com.callerid.model;

import java.util.ArrayList;

public class IgnoreListResponse {
   public int statusCode;
   public boolean status;
   public ArrayList<Datum> data;

   public int getStatusCode() {
      return statusCode;
   }

   public boolean isStatus() {
      return status;
   }

   public ArrayList<Datum> getData() {
      return data;
   }

   public class Datum{
      public String _id;
      public String phone;
      public String createdBy;
      public String createdAt;
      public String updatedAt;
      public int __v;

      public String get_id() {
         return _id;
      }

      public String getPhone() {
         return phone;
      }

      public String getCreatedAt() {
         return createdAt;
      }
   }


}

