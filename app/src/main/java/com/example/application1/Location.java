package com.example.application1;

public class Location {
    String email, date, time, user_id;
    int timestamp;
    double latitude, longtitude;


    public Location() {

    }

    public Location(String email, String date, String time, String user_id, int timestamp, double latitude, double longtitude) {
        this.email = email;
        this.date = date;
        this.time = time;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
