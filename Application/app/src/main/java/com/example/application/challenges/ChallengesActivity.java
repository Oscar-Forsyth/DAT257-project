package com.example.application.challenges;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChallengesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Challenge> challenges;
    private List<Challenge> missions = new ArrayList<>();
    private List<Challenge> longChallenges = new ArrayList<>();

    private final static String JSON_URL = "https://www.googleapis.com/calendar/v3/calendars/c6isg5rcllc2ki81mnpnv92g90@group.calendar.google.com/events?key=AIzaSyAfe6owfkgrW0GjN5c3N_DDLELAHagbKEg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        this.setTitle("Challenges");

        recyclerView = findViewById(R.id.challengesList);
        challenges = new ArrayList<>();
        extractChallenges();
        TextView textView = (TextView) findViewById(R.id.toolbarText);
        textView.setText("Challenges");
    }

    /**
     * Extract information from CIS Google Calendar and add the information to the challenge tab.
     */
    private void extractChallenges() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                addChallengesFromJSON(response);
                sortChallenges();
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

    private void addChallengesFromJSON(JSONObject response) {

        try {
            JSONArray items = response.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                Challenge challenge = new Challenge();
                try {
                    challenge.setTitle(items.getJSONObject(i).getString("summary"));
                }   catch (JSONException e) {
                    challenge.setTitle("CIS Challenge");
                }
                try {
                    challenge.setStartDate(items.getJSONObject(i).getJSONObject("start").getString("dateTime"));
                } catch (JSONException e) {
                    challenge.setStartDate(items.getJSONObject(i).getJSONObject("start").getString("date"));
                }
                try {
                    challenge.setEndDate(items.getJSONObject(i).getJSONObject("end").getString("dateTime"));
                } catch (JSONException e) {
                    challenge.setEndDate(items.getJSONObject(i).getJSONObject("end").getString("date"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    String location = items.getJSONObject(i).getString("location");
                    String[] res = location.split("[,]", 0);
                    challenge.setLocation(res[0]);
                } catch (JSONException e) {
                    challenge.setLocation(" ");
                }
                try {
                    String description = items.getJSONObject(i).getString("description");
                    challenge.setDescription(description);
                } catch (JSONException e) {
                    challenge.setDescription(" ");
                }
                challenges.add(challenge);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void sortChallenges() {
        Collections.sort(challenges, new Comparator<Challenge>() {
            @Override
            public int compare(Challenge object1, Challenge object2) {
                return object1.getStartDate().compareTo(object2.getStartDate());
            }
        });
    }

    private void addToLayout() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        missions.clear();
        missions.addAll(challenges);
        ChallengesAdapter adapter = new ChallengesAdapter(getApplicationContext(), missions);
        recyclerView.setAdapter(adapter);
    }

    // Not optimized at all...
    public void displayMissions(View view) {
        missions.clear();
        missions.addAll(challenges);
        ChallengesAdapter adapter = new ChallengesAdapter(getApplicationContext(), missions);
        recyclerView.setAdapter(adapter);
    }

    // Not optimized at all...
    public void displayWeeklyChallenges(View view) {
        longChallenges.clear();
        //TODO add our own challenges
        ChallengesAdapter adapter = new ChallengesAdapter(getApplicationContext(), longChallenges);
        recyclerView.setAdapter(adapter);
    }
    public void displayActive(View view){
        
    }
    public void displayCompleted(View view){

    }
}