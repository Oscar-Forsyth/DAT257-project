package com.example.application;

public class Activity {
    private String title;
    private String date;
    private String location;

    public Activity(){}

    public Activity(String title, String date, String description){
        this.title = title;
        this.date = date;
        this.location= location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
