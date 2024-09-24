package com.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.TelephonyManager;

import java.util.Locale;

public class AsSqlLite extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "caller_id";
    private Context mContext;

    public AsSqlLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Query =
                "create table MyIgnoreList(sr INTEGER PRIMARY KEY AUTOINCREMENT,id Text,name Text,phone Text,createdAt Text)";
        db.execSQL(Query);

    }

    public void insertIgnoreList(String id, String name, String phone, String createdAt) {
        Cursor cr = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("id", id);
            initialValues.put("name", name);
            initialValues.put("phone", phone);
            initialValues.put("createdAt", createdAt);
            String selectQuery = "SELECT phone FROM MyIgnoreList where phone='" + phone + "'";
            cr = db.rawQuery(selectQuery, null);
            if (cr.moveToFirst())
                db.update("MyIgnoreList", initialValues, "phone=?", new String[]{phone});
            else
                db.insert("MyIgnoreList", null, initialValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cr.close();
        }
    }


    public Cursor getAllIgnoreList() {
        Cursor cr = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "select * from MyIgnoreList";
            cr = db.rawQuery(query, null);
        } catch (Exception e) {
        }
        return cr;
    }

    public void DeleteGroupSrTb(String groupTBsr) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String Query = "delete from CategoryTB where groupSr='" + groupTBsr + "'";
            db.execSQL(Query);
        } catch (Exception e) {
        }
    }

    public String getNumber() {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT phone FROM MyIgnoreList";
            Cursor cursor = db.rawQuery(selectQuery, null);
            //return "1800005437";
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getDOB() {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT DOB FROM UserTB";
            Cursor cursor = db.rawQuery(selectQuery, null);
            //return "1800005437";
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getLoginUserid() {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT LoginUserid FROM UserTB";
            Cursor cursor = db.rawQuery(selectQuery, null);
            //return "1800005437";
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getMobile() {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT Mobile FROM UserTB";
            Cursor cursor = db.rawQuery(selectQuery, null);
            //return "1800005437";
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }


    public String getGender() {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT Gender FROM MyIgnoreList";
            Cursor cursor = db.rawQuery(selectQuery, null);
            //return "1800005437";
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getParentId() {
        //return "1800006991";
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT ParentId FROM UserTB";
            Cursor cursor = db.rawQuery(selectQuery, null);
            //return "1800005437";
            if (cursor.moveToFirst()) {
                if (cursor.getString(0) != null)
                    return cursor.getString(0);
                else return "0";
            } else {
                return "0";
            }
        } catch (Exception e) {
            return "0";
        }

    }

    public String getuserId() {
        //return "3014";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT UserId FROM UserTB";
            Cursor cursor = db.rawQuery(selectQuery, null);
            //return "1800005437";
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            } else {
                return "0";
            }
        } catch (Exception e) {
            return "0";
        }
    }




    public String getcountryname(String Countrycode) {
        String str = "0";
        Cursor cr = null;
        try {
            String selectQuery = "SELECT CountryName  from CountrycodeTB where Code='" + Countrycode + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            cr = db.rawQuery(selectQuery, null);
            if (cr.moveToFirst()) {
                str = cr.getString(0);
            }
        } catch (Exception e) {
        }
        return str;
    }


    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return "0";
    }

    public String getcountrycode(String CountryName) {
        String str = "0";
        Cursor cr = null;
        try {
            String selectQuery = "SELECT Code from CountrycodeTB where CountryName='" + CountryName + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            cr = db.rawQuery(selectQuery, null);
            if (cr.moveToFirst()) {
                str = cr.getString(0);
            }
            cr.close();
        } catch (Exception e) {
        }
        return str;
    }


    //End CountryCode
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        if (newVersion > oldVersion) { //sizeName text


            try {
                String Query =
                        "create table MyIgnoreList(sr INTEGER PRIMARY KEY AUTOINCREMENT,id Text,name Text,phone Text,createdAt Text)";
                db.execSQL(Query);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }
}
