package com.example.application.calendar;

/**
 * Class that holds information about one Activity. It also has methods
 * to retrieve and set useful information regarding activities.
 */
public class Activity {
    private String title;
    private String date;
    private String prettyDate;
    private String location;

    public Activity(){}

    /** Creates an activity with the specified title, date and location.
     * @param title The activity’s title.
     * @param date The activity’s date.
     * @param location The activity's location.
     */
    public Activity(String title, String date, String location){
        this.title = title;
        this.date = date;
        // Converts date to more readable format
        prettyDate = DateConverter.convertDateFromCalendar(date);
        this.location= location;
    }

    /** Gets the activity’s title.
     * @return A string representing the activity’s title.
     */
    public String getTitle() {
        return title;
    }

    /** Sets the activity’s title.
     * @param title A String containing the activity’s title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Gets the activity’s date.
     * @return A string representing the activity’s date.
     */
    public String getDate() {
        return date;
    }

    /** Gets the activity’s date in more readable format.
     * @return A string representing the activity’s date.
     */
    public String getPrettyDate() {
        return prettyDate;
    }

    /** Sets the activity’s date.
     * @param date A String containing the activity’s date in original format.
     */
    public void setDate(String date) {
        this.date = date;
        prettyDate = DateConverter.convertDateFromCalendar(date);
    }

    /** Gets the activity’s location.
     * @return A string representing the activity’s location.
     */
    public String getLocation() {
        return location;
    }

    /** Sets the activity’s location.
     * @param location A String containing the activity’s location.
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
