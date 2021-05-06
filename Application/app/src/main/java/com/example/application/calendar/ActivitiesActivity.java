package com.example.application.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Activity responsible for the information that can later be used in the Upcoming Events tab
 */
public class ActivitiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Activity> activities;
    private List<Activity> specificActivities;

    private final static String JSON_URL = "https://www.googleapis.com/calendar/v3/calendars/cis-chalmers.se_295gphnnjamvidi831rg4f0120@group.calendar.google.com/events?key=AIzaSyAfe6owfkgrW0GjN5c3N_DDLELAHagbKEg";

    private Toolbar toolbar;
    private TextView textView, monthView;
    private CompactCalendarView calendarView;
    private String savedDate;
    private SimpleDateFormat sdf;
    private SimpleDateFormat calendarTextHeader;
    private ConstraintLayout calendarConstraint;

    private ImageButton calendarLeft, calendarRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        this.setTitle("Activities");

        recyclerView = findViewById(R.id.activitiesList);
        activities = new ArrayList<>();
        extractActivities();

        calendarView = findViewById(R.id.calendarView);


        findViewById(R.id.calendarHeader).setVisibility(View.GONE);

        calendarTextHeader = new SimpleDateFormat("MMMM yyyy");


        monthView = findViewById(R.id.monthYear);
        monthView.setText(calendarTextHeader.format(System.currentTimeMillis()));


        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                showTodaysActivities(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                String monthString = calendarTextHeader.format(calendarView.getFirstDayOfCurrentMonth());
                monthView.setText(monthString);
            }
        });

        activities.add(new Activity("test test", "2021-05-05", "Hemma hos mig")); //temp
        activities.add(new Activity("test imorgon", "2021-05-06", "Hemma hos dig")); //temp

        long date = Calendar.getInstance().getTimeInMillis();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        savedDate = sdf.format(date);

        Event er = new Event(Color.BLUE, System.currentTimeMillis());
        calendarView.addEvent(er);
        calendarView.setUseThreeLetterAbbreviation(true);

        //Left and right button for calendar
        calendarLeft = findViewById(R.id.calendarLeft);
        calendarRight = findViewById(R.id.calendarRight);

        calendarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.scrollLeft();
            }
        });

        calendarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.scrollRight();
            }
        });



        toolbar = findViewById(R.id.customToolbar);
        textView = (TextView) findViewById(R.id.toolbarText);
        textView.setText("Upcoming Events");


    }

    private void showTodaysActivities(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        savedDate = formattedDate;
        compareDate(formattedDate);
    }

    private void showTodaysActivities(String date) {
        compareDate(date);
    }

    private void compareDate(String date) {
        specificActivities = new ArrayList<>();
        for(Activity a : activities) {
            if (a.getDate().substring(0,10).equals(date)) {
                specificActivities.add(a);
            }
        }
        ActivitiesAdapter adapter = new ActivitiesAdapter(getApplicationContext(), specificActivities);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Extract information from CIS Google Calendar and add the information to the activity tab.
     */
    private void extractActivities() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                addActivitiesFromJSON(response);
                sortActivities();
                addToLayout();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }

        });
        queue.add(jsonObjectRequest);
    }

    private void addActivitiesFromJSON(JSONObject response) {
        try {
            JSONArray items = response.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                addJSONObjectToActivities(items.getJSONObject(i));
            }
            for (Activity a : activities) {
                try {
                    Date d = sdf.parse(a.getDate().substring(0,10));
                    Event e = new Event(Color.parseColor("#FFDD7B19"), d.getTime());
                    calendarView.addEvent(e);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addJSONObjectToActivities(JSONObject item) {
        Activity activity = new Activity();
        String startDate;
        try {
            activity.setTitle(item.getString("summary"));
        }   catch (JSONException e) {
            activity.setTitle("CIS Aktivitet");
        }
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
        activity.setDate(startDate);
        try {
            String location = item.getString("location");
            String[] res = location.split("[,]", 0);
            activity.setLocation(res[0]);
            //activity.setLocation(items.getJSONObject(i).getString("location"));
        } catch (JSONException e) {
            activity.setLocation("Location unknown");
        }
        try {
            String recurrence = item.getString("recurrence");
            DateFormat until = new SimpleDateFormat("yyyyMMdd");
            DateFormat from = new SimpleDateFormat("yyyy-MM-dd");
            Date lastDate = until.parse(recurrence.substring(26,34));
            Date currentDate = from.parse(startDate.substring(0,10));
            currentDate = addDaysToDate(currentDate, 7);
            while (currentDate.compareTo(lastDate) < 0) {
                JSONObject newActivity = new JSONObject();
                newActivity.put("start", from.format(currentDate)+startDate.substring(10));
                newActivity.put("summary", item.getString("summary"));
                try {
                    newActivity.put("location", item.getString("location"));
                } catch (JSONException noLoc) {
                    // skip location
                }
                addJSONObjectToActivities(newActivity);
                currentDate = addDaysToDate(currentDate, 7);
            }
        } catch (JSONException | ParseException e) {
            // no recurrence, nice
        }
        activities.add(activity);
    }

    private Date addDaysToDate(Date date, int noOfDays) {
        Date newDate = new Date(date.getTime());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(newDate);
        calendar.add(Calendar.DATE, noOfDays);
        newDate.setTime(calendar.getTime().getTime());
        return newDate;
    }

    private void sortActivities() {
        Collections.sort(activities, new Comparator<Activity>() {
            @Override
            public int compare(Activity object1, Activity object2) {
                return object1.getDate().compareTo(object2.getDate());
            }
        });
    }

    private void addToLayout() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ActivitiesAdapter adapter = new ActivitiesAdapter(getApplicationContext(), activities);
        recyclerView.setAdapter(adapter);
    }
    public void goBack(View view){
        this.onBackPressed();
    }

    public void changeToFlow(View view) {
        findViewById(R.id.calendarHeader).setVisibility(View.GONE);
        ActivitiesAdapter adapter = new ActivitiesAdapter(getApplicationContext(), activities);
        recyclerView.setAdapter(adapter);
    }

    public void changeToCalendar(View view) {
        findViewById(R.id.calendarHeader).setVisibility(View.VISIBLE);
        showTodaysActivities(savedDate);
    }
}