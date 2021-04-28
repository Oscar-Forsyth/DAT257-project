package com.example.application.challenges;

import com.example.application.calendar.DateConverter;

public class Challenge {
    private String title;
    private String startDate;
    private String endDate;
    private String prettyStartDate;
    private String prettyEndDate;
    private String location;

    public Challenge(){}

    /** Creates an activity with the specified title, date and location.
     * @param title The activity’s title.
     * @param startDate The activity’s start date.
     * @param location The activity's location.
     */
    public Challenge(String title, String startDate, String endDate, String location){
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        // Converts date to more readable format
        prettyStartDate = DateConverter.convertDateFromCalendarWithoutTime(startDate);
        prettyEndDate = DateConverter.convertDateFromCalendarWithoutTime(endDate);
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
    public String getStartDate() {
        return startDate;
    }

    /** Gets the activity’s start date in more readable format.
     * @return A string representing the activity’s start date.
     */
    public String getPrettyStartDate() {
        return prettyStartDate;
    }

    /** Sets the activity’s start date.
     * @param startDate A String containing the activity’s start date in original format.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
        prettyStartDate = DateConverter.convertDateFromCalendarWithoutTime(startDate);
    }

    public String getEndDate() {
        return endDate;
    }

    /** Gets the activity’s end date in more readable format.
     * @return A string representing the activity’s end date.
     */
    public String getPrettyEndDate() {
        return prettyEndDate;
    }

    /** Sets the activity’s end date.
     * @param endDate A String containing the activity’s end date in original format.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
        prettyEndDate = DateConverter.convertDateFromCalendarWithoutTime(endDate);
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
