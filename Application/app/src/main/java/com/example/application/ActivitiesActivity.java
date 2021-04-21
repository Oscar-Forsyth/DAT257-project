package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivitiesActivity extends AppCompatActivity {

    /**
     * the framework of the list displaying the sports
     */
    RecyclerView recyclerView;
    List<Activity> activities;

    /**
     * the URL for our JSON-file
     * For every update to the JSON-file, a new URL has to be generated so there is probably a better solution
     */

    private static String JSON_URL = "https://www.googleapis.com/calendar/v3/calendars/cis-chalmers.se_295gphnnjamvidi831rg4f0120@group.calendar.google.com/events?key=AIzaSyAfe6owfkgrW0GjN5c3N_DDLELAHagbKEg";
    ActivitiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        this.setTitle("Activities");

        recyclerView = findViewById(R.id.activitiesList);
        activities = new ArrayList<>();
        try {
            extractActivities();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * JSON content is read from local file
     */

    private String loadJSONFromAsset() throws JSONException {
        String json = null;
        try {
            InputStream is = getAssets().open("activities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



    /**
     * JSON content is translated from loadJSONFromAsset
     */
    private void extractActivities() throws JSONException {

        //Link to URL - Saved for google API

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                            activity.setDescription(items.getJSONObject(i).getString("description"));
                        } catch (JSONException e) {
                            activity.setDescription("hej hallå");
                        }

                        activities.add(activity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new ActivitiesAdapter(getApplicationContext(),activities);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }

        });

        queue.add(jsonObjectRequest);



    }
}