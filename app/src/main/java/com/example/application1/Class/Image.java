package com.example.application1.Class;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.util.List;
import java.util.Map;

public class Image implements Serializable {
    String date_stamp;

    public Image() {

    }

    public Image(String date_stamp) {
        this.date_stamp = date_stamp;
    }

    public String getDate_stamp() {
        return date_stamp;
    }

    public void setDate_stamp(String date_stamp) {
        this.date_stamp = date_stamp;
    }
}
