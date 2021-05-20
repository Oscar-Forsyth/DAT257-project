package com.example.application.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.R;
import com.example.application.sports.Sport;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Activity responsible for the Upcoming Events tab which consists of a flow-view with events and
 * a calendar that displays the events in an alternative way.
 */
public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Event> events;
    private final List<Event> currentEvents = new ArrayList<>();
    private final List<Event> dayEvents = new ArrayList<>();
    private List<Sport> recommendedSports;
    private List<Sport> favouriteSports;
    private boolean isCalenderViewOpen = false;

    private final EventsJSONConverter eventsJSONConverter = new EventsJSONConverter();
    private final static String JSON_URL = "https://www.googleapis.com/calendar/v3/calendars/cis-chalmers.se_295gphnnjamvidi831rg4f0120@group.calendar.google.com/events?key=AIzaSyAfe6owfkgrW0GjN5c3N_DDLELAHagbKEg";

    private TextView headerTitle, monthView, noEventsText;
    private CheckBox recommendedFilterButton, favouriteFilterButton;
    private CompactCalendarView calendarView;
    private String savedDate;
    private SimpleDateFormat sdf;
    private SimpleDateFormat calendarTextHeader;
    private ImageButton calendarLeft, calendarRight;

    /**
     * Method that runs when the activity is created and initiate what is displayed in the activity.
     * @param savedInstanceState of type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        this.setTitle("Events");

        coupleVariablesWithLayout();
        initiateVariables();
        addButtonListeners();

        extractActivities();
    }

    /**
     * Method that goes back to the latest activity.
     * @param view a View
     */
    public void goBack(View view){
        this.onBackPressed();
    }

    /**
     * Method that displays events in a flow-list.
     * @param view a View
     */
    public void changeToFlow(View view) {
        isCalenderViewOpen = false;
        findViewById(R.id.calendarHeader).setVisibility(View.GONE);
        filterActivitiesList();
        updateActivitiesInList(currentEvents);
        scrollToToday();
    }

    /**
     * Method that displays events in a calendar.
     * @param view a View
     */
    public void changeToCalendar(View view) {
        isCalenderViewOpen = true;
        findViewById(R.id.calendarHeader).setVisibility(View.VISIBLE);
        filterActivitiesList();
        showTodaysActivities(savedDate);
    }

    // ---- Private methods ------------------------------------------------------------------------

    private void extractActivities() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                events = eventsJSONConverter.getEventsFromJSON(response);
                sortActivities();
                currentEvents.addAll(events);
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

    private void addButtonListeners() {
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
        recommendedFilterButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                filterUpdated();
            }
        });
        favouriteFilterButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                filterUpdated();
            }
        });
    }

    private void coupleVariablesWithLayout() {
        recyclerView = findViewById(R.id.activitiesList);
        calendarView = findViewById(R.id.calendarView);
        monthView = findViewById(R.id.monthYear);
        calendarLeft = findViewById(R.id.calendarLeft);
        calendarRight = findViewById(R.id.calendarRight);
        headerTitle = findViewById(R.id.toolbarText);
        recommendedFilterButton = findViewById(R.id.recommendedFilterButton);
        favouriteFilterButton = findViewById(R.id.favouriteFilterButton);
        noEventsText = findViewById(R.id.noEventsText);
    }

    private void initiateVariables() {
        events = new ArrayList<>();
        findViewById(R.id.calendarHeader).setVisibility(View.GONE);
        calendarTextHeader = new SimpleDateFormat("MMMM yyyy");
        calendarView.setUseThreeLetterAbbreviation(true);
        monthView.setText(calendarTextHeader.format(System.currentTimeMillis()));
        long date = Calendar.getInstance().getTimeInMillis();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        savedDate = sdf.format(date);
        headerTitle.setText("Upcoming Events");
        initiateRecAndFavSports();
    }

    private void initiateRecAndFavSports() {
        recommendedSports = new ArrayList<>();
        Sport badminton = new Sport();
        badminton.setEmail("ida.hoglundpersson@cis-chalmers.se");
        Sport basket = new Sport();
        basket.setEmail("basket@cis-chalmers.se");
        Sport innebandy = new Sport();
        innebandy.setEmail("jonathan.stalberg@cis-chalmers.se");
        Sport tennis = new Sport();
        tennis.setEmail("tennis@cis-chalmers.se");
        Sport volley = new Sport();
        volley.setEmail("jonathan.stalberg@cis-chalmers.se");
        recommendedSports.add(badminton);
        recommendedSports.add(basket);
        recommendedSports.add(innebandy);
        recommendedSports.add(tennis);
        recommendedSports.add(volley);
        favouriteSports = new ArrayList<>();
        favouriteSports.add(badminton);
    }

    private void filterUpdated() {
        filterActivitiesList();
        updateDotsOnCalendar();
        if(isCalenderViewOpen) {
            showTodaysActivities(savedDate);
        } else {
            updateActivitiesInList(currentEvents);
        }
        scrollToToday();
    }

    private void filterActivitiesList() {
        currentEvents.clear();
        if (recommendedFilterButton.isChecked()) {
            findActivitiesFromSports(recommendedSports);
        }
        if (favouriteFilterButton.isChecked()) {
            findActivitiesFromSports(favouriteSports);
        }
        if (!(recommendedFilterButton.isChecked() || favouriteFilterButton.isChecked())) {
            currentEvents.addAll(events);
        }

    }

    private void findActivitiesFromSports(List<Sport> sports) {
        for (Event e : events) {
            for (Sport s : sports) {
                if (s.getEmail().equals(e.getCreator()) && !currentEvents.contains(e)) {
                    currentEvents.add(e);
                    break;
                }
            }
        }
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
        dayEvents.clear();
        for(Event a : currentEvents) {
            if (a.getDate().substring(0,10).equals(date)) {
                dayEvents.add(a);
            }
        }
        updateActivitiesInList(dayEvents);
    }

    private void updateActivitiesInList(List<Event> activities){
        if (activities.isEmpty()){
            noEventsText.setVisibility(View.VISIBLE);
        } else {
            noEventsText.setVisibility(View.INVISIBLE);
        }
        EventsAdapter adapter = new EventsAdapter(getApplicationContext(), activities);
        recyclerView.setAdapter(adapter);
    }

    private void sortActivities() {
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event object1, Event object2) {
                return object1.getDate().compareTo(object2.getDate());
            }
        });
    }

    private void addToLayout() {
        updateDotsOnCalendar();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        updateActivitiesInList(currentEvents);
        scrollToToday();
    }

    private void scrollToToday() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = df.format(date);
        int i;
        for (i = 0; i < currentEvents.size()-1; i++) {
            if(currentEvents.get(i).getDate().compareTo(currentDate) >= 0) {
                break;
            }
        }
        recyclerView.scrollToPosition(i);
    }

    private void updateDotsOnCalendar() {
        calendarView.removeAllEvents();
        for (Event a : currentEvents) {
            try {
                Date d = sdf.parse(a.getDate().substring(0,10));
                com.github.sundeepk.compactcalendarview.domain.Event e = new com.github.sundeepk.compactcalendarview.domain.Event(Color.parseColor("#FFDD7B19"), d.getTime());
                calendarView.addEvent(e);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}