package com.example.application.challenges;

import com.example.application.calendar.DateConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Represents either a CIS-Mission or a Daily Challenge
 */
public class Challenge {
    private String title;
    private String endDate;
    private String prettyEndDate;
    private String location;
    private String description;
    private boolean completed;

    public Challenge(){}

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /** Gets the challenge title.
     * @return A string representing the challenge title.
     */
    public String getTitle() {
        return title;
    }

    /** Sets the challenge title.
     * @param title A String containing the challenge title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndDate() {
        return endDate;
    }

    /** Gets the challenge end date in more readable format.
     * @return A string representing the challenge end date.
     */
    public String getPrettyEndDate() {
        return prettyEndDate;
    }

    /** Sets the challenge end date.
     * @param endDate A String containing the challenge end date in original format.
     */
    public void setEndDate(String endDate) throws ParseException {
        this.endDate = endDate;

        if(endDate.length() == 10) {    //Date is on the format "date", eg. yyyy-MM-dd

            //Convert from string to date
            String pattern = "yyyy-MM-dd";
            Date endDateDate = new SimpleDateFormat(pattern).parse(endDate);

            //Decrement date with one day
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDateDate);
            cal.add(Calendar.DATE, -1);
            endDateDate = cal.getTime();

            //Convert back to string
            DateFormat df = new SimpleDateFormat(pattern);
            endDate = df.format(endDateDate);
        }
        else {          //Date is on the format "dateTime"
        }
        prettyEndDate = DateConverter.convertDateFromCalendarWithoutTime(endDate);
    }

    /** Gets the challenge location.
     * @return A string representing the challenge location.
     */
    public String getLocation() {
        return location;
    }

    /** Sets the challenge location.
     * @param location A String containing the challenge location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /** Gets the challenge description.
     * @return A string representing the challenge description.
     */
    public String getDescription() { return description; }

    /** Sets the challenge location.
     * @param description A String containing the challenge description.
     */
    public void setDescription(String description) { this.description = description;
    }
}