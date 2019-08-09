package com.example.application1.Class;

import java.io.Serializable;

public class ImageItself implements Serializable {
    String date_stamp, full_name, image_url;

    public ImageItself() {

    }

    public ImageItself(String date_stamp, String full_name, String image_url) {
        this.date_stamp = date_stamp;
        this.full_name = full_name;
        this.image_url = image_url;
    }

    public String getDate_stamp() {
        return date_stamp;
    }

    public void setDate_stamp(String date_stamp) {
        this.date_stamp = date_stamp;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
