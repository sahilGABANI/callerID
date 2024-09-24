package com.callerid.model;

public class StatusModel {
    private String name, color, value;
    private boolean idDefault;

    public StatusModel(/*String name, String color, String value, boolean isSaleStatus*/) {
//        this.name = name;
//        this.color = color;
//        this.value = value;
//        this.idDefault = isSaleStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSaleStatus() {
        return idDefault;
    }

    public void setSaleStatus(boolean saleStatus) {
        idDefault = saleStatus;
    }
}