package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

    private static String JSON_URL = "http://www.json-generator.com/api/json/get/cpXYVruRsO?indent=2";
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

        JSONArray arr = new JSONArray(loadJSONFromAsset());

        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject activityObject = arr.getJSONObject(i);

                Activity activity = new Activity();
                activity.setTitle(activityObject.getString("title").toString());
                activity.setDate(activityObject.getString("date"));
                activity.setDescription(activityObject.getString("description".toString()));
                activities.add(activity);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ActivitiesAdapter(getApplicationContext(),activities);
        recyclerView.setAdapter(adapter);


        //Link to URL - Saved for google API

        /*
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject sportObject = response.getJSONObject(i);

                        Sport sport = new Sport();
                        sport.setName(sportObject.getString("name").toString());
                        sport.setDescription(sportObject.getString("description".toString()));
                        sport.setLogo(sportObject.getString("logo"));
                        sports.add(sport);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new Adapter(getApplicationContext(),sports);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }

        });

        queue.add(jsonArrayRequest);


         */
    }
}