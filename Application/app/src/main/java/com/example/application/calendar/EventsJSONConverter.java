package com.example.application.calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class EventsJSONConverter {

    private final List<Event> events = new ArrayList<>();

    private final DateFormat JSONFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final DateFormat recurrenceFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * Method that converts an Google Calendar to a list of events.
     * @param response the entire Google Calendar JSONObject
     * @return a list with all events
     */
    protected List<Event> getEventsFromJSON(JSONObject response) {
        try {
            JSONArray items = response.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                addJSONObjectToEvents(items.getJSONObject(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return events;
    }

    /**
     * Method that transform one event in the Google Calender to an event of type Event and adds it
     * to the list of all events.
     * @param item one event in the Google Calendar
     */
    private void addJSONObjectToEvents(JSONObject item) {
        Event event = new Event();
        String startDate;
        // Get the events title
        try {
            event.setTitle(item.getString("summary"));
        }   catch (JSONException e) {
            event.setTitle("CIS Aktivitet");
        }
        // Get the events date and time, or only date if there is no specified time
        try {
            startDate = item.getJSONObject("start").getString("dateTime");
        } catch (JSONException noTime) {
            try {
                startDate = item.getJSONObject("start").getString("date");
            } catch (JSONException noDate) {
                try {
                    startDate = item.getString("start");
                } catch (JSONException notRecurring) {
                    startDate = "1970-01-01";
                }
            }
        }
        event.setDate(startDate);
        // Get the most specific part of the events location
        try {
            String location = item.getString("location");
            String[] res = location.split("[,]", 0);
            event.setLocation(res[0]);
        } catch (JSONException e) {
            event.setLocation("Location unknown");
        }
        // Get the events description if there is one
        try {
            String description = item.getString("description");
            event.setDescription(description);
        } catch (JSONException e) {
            event.setDescription(" ");
        }
        // Get the events creator if there is one
        try {
            String creator = item.getJSONObject("creator").getString("email");
            event.setCreator(creator);
        } catch (JSONException e) {
            event.setCreator("Unknown");
        }
        // Add recurring events
        try {
            String recurrence = item.getString("recurrence");
            Date lastDate = recurrenceFormat.parse(recurrence.substring(26,34));
            addNewEvents(item, startDate, lastDate, 7);
        } catch (JSONException | ParseException e) {
            // no recurrence, nice
        }
        // Add events that are longer than one day
        try {
            Date endDate = JSONFormat.parse(item.getJSONObject("end").getString("date"));
            if (!startDate.equals(endDate)) {
                addNewEvents(item, startDate, endDate, 1);
            }
        } catch (JSONException | ParseException e) {
            // do nothing
        }
        events.add(event);
    }

    /**
     * Method that creates new events based on the first event in a series of recurring events.
     * @param item the original event
     * @param startDate the date of the original event
     * @param lastDate the date of the last event in the series
     * @param daysBetweenEachNewEvent the number of days between each new event
     * @throws ParseException formats a string to a Date
     * @throws JSONException tries to get JSON objects
     */
    private void addNewEvents(JSONObject item, String startDate, Date lastDate, int daysBetweenEachNewEvent) throws ParseException, JSONException {
        Date currentDate = JSONFormat.parse(startDate.substring(0,10));
        currentDate = addDaysToDate(currentDate, daysBetweenEachNewEvent);
        while (currentDate.compareTo(lastDate) < 0) {
            JSONObject newActivity = new JSONObject();
            newActivity.put("start", JSONFormat.format(currentDate)+startDate.substring(10));
            newActivity.put("summary", item.getString("summary"));
            try {
                newActivity.put("location", item.getString("location"));
            } catch (JSONException noLoc) {
                // skip location
            }
            try {
                newActivity.put("description", item.getString("description"));
            } catch (JSONException noDes) {
                // skip description
            }
            try {
                JSONObject email = new JSONObject();
                email.put("email", item.getJSONObject("creator").getString("email"));
                newActivity.put("creator", email);
            } catch (JSONException noCre) {
                // skip creator
            }
            addJSONObjectToEvents(newActivity);
            currentDate = addDaysToDate(currentDate, daysBetweenEachNewEvent);
        }
    }

    /**
     * Method that calculates a new date based on a previous date.
     * @param date the previous date
     * @param noOfDays number of days to the desired date
     * @return the new date x days after the original date
     */
    private Date addDaysToDate(Date date, int noOfDays) {
        Date newDate = new Date(date.getTime());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(newDate);
        calendar.add(Calendar.DATE, noOfDays);
        newDate.setTime(calendar.getTime().getTime());
        return newDate;
    }
}
