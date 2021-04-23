package com.example.application.calendar;

/**
 * Class that holds information about one Activity.
 */
public class Activity {
    private String title;
    private String date;
    private String prettyDate;
    private String location;

    public Activity(){}

    public Activity(String title, String date, String location){
        this.title = title;
        this.date = date;
        prettyDate = DateConverter.convertDateFromCalendar(date);
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

    public String getPrettyDate() {
        return prettyDate;
    }

    public void setDate(String date) {
        this.date = date;
        prettyDate = DateConverter.convertDateFromCalendar(date);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
