package com.example.application.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Activity responsible for the information that can later be used in the Upcoming Events tab
 */
public class ActivitiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Activity> activities;

    private final static String JSON_URL = "https://www.googleapis.com/calendar/v3/calendars/cis-chalmers.se_295gphnnjamvidi831rg4f0120@group.calendar.google.com/events?key=AIzaSyAfe6owfkgrW0GjN5c3N_DDLELAHagbKEg";

    private Toolbar toolbar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        this.setTitle("Activities");

        recyclerView = findViewById(R.id.activitiesList);
        activities = new ArrayList<>();
        extractActivities();

        toolbar = findViewById(R.id.customToolbar);
        textView = (TextView) findViewById(R.id.toolbarText);
        textView.setText("Upcoming Events");
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
                Activity activity = new Activity();
                try {
                    activity.setTitle(items.getJSONObject(i).getString("summary"));
                }   catch (JSONException e) {
                    activity.setTitle("CIS Aktivitet");
                }
                try {
                    activity.setDate(items.getJSONObject(i).getJSONObject("start").getString("dateTime"));
                } catch (JSONException e) {
                    activity.setDate(items.getJSONObject(i).getJSONObject("start").getString("date"));
                }
                try {
                    String location = items.getJSONObject(i).getString("location");
                    String[] res = location.split("[,]", 0);
                    activity.setLocation(res[0]);
                } catch (JSONException e) {
                    activity.setLocation("Location unknown");
                }
                try {
                    String description = items.getJSONObject(i).getString("description");
                    activity.setDescription(description);
                } catch (JSONException e) {
                    activity.setDescription(" ");
                }
                activities.add(activity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}