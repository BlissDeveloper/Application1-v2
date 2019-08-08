package com.example.application1.Class;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.util.List;
import java.util.Map;

public class Image implements Serializable {
    List<Map<String, Object>> array_url;
    String datestamp;

    public Image() {

    }

    public Image(List<Map<String, Object>> array_url, String datestamp) {
        this.array_url = array_url;
        this.datestamp = datestamp;
    }

    public List<Map<String, Object>> getArray_url() {
        return array_url;
    }

    public void setArray_url(List<Map<String, Object>> array_url) {
        this.array_url = array_url;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }
}
