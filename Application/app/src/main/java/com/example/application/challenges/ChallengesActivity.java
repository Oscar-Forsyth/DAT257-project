package com.example.application.challenges;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.R;
import com.example.application.Tag;
import com.example.application.sports.Sport;
import com.example.application.sports.SportsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChallengesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private final List<Challenge> challenges = new ArrayList<>();;
    private final List<Challenge> missions = new ArrayList<>();
    private final List<Challenge> allDailyChallenges = new ArrayList<>();
    private final List<Challenge> currentDailyChallenges = new ArrayList<>();
    private final List<Challenge> completedDailyChallenges = new ArrayList<>();

    private boolean isOnMissions = true;

    private final static String JSON_URL = "https://www.googleapis.com/calendar/v3/calendars/c6isg5rcllc2ki81mnpnv92g90@group.calendar.google.com/events?key=AIzaSyAfe6owfkgrW0GjN5c3N_DDLELAHagbKEg";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        this.setTitle("Challenges");

        TextView textView =  findViewById(R.id.toolbarText);
        textView.setText("Challenges");

        recyclerView = findViewById(R.id.challengesList);

        extractChallenges();

        //creates daily challenges based on jsonChallenges
        try {
            extractChallenges2();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void extractSavedMissions(){
        SharedPreferences completedMissions = getSharedPreferences("CompletedMissions", MODE_PRIVATE);
        Set<String> set = completedMissions.getStringSet("completedMission",null);
        for (int i = 0; i < set.size(); i++) {
            System.out.println("SET:" + i + ":" +set.toArray()[i].toString());
        }
        for (int i = 0; i < set.size(); i++) {
            for (int j = 0; j < missions.size(); j++) {
                System.out.println(set.toArray()[i].toString()+ ":" +missions.get(j).getTitle());
                if(set.toArray()[i].toString().equals(missions.get(j).getTitle())){
                    if(missions.get(j).isCompleted()){
                        missions.get(j).setCompleted(false);
                    }else{
                        missions.get(j).setCompleted(true);
                    }

                    System.out.println("accepted");
                    break;
                }
            }

        }
    }

    public void saveCompletedMission(){
        SharedPreferences.Editor editor = getSharedPreferences("CompletedMissions", MODE_PRIVATE).edit();
        Set<String> savedMissionsSet = new HashSet<>();
        for (Challenge c : challenges) {
            if(c.isCompleted()){
                savedMissionsSet.add(c.getTitle());
            }
        }
        editor.putStringSet("completedMission",savedMissionsSet);
        editor.apply();
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

                extractSavedMissions();
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
            missions.addAll(challenges);
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
    /**
     * JSON content is read from local file
     */
    private String loadJSONFromAsset()  {
        String json = null;
        try {
            InputStream is = getAssets().open("jsonChallenges");
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
    private void extractChallenges2() throws JSONException {

        JSONArray arr = new JSONArray(loadJSONFromAsset());

        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject challengeObject = arr.getJSONObject(i);

                Challenge challenge = new Challenge();
                challenge.setTitle(challengeObject.getString("title"));
                challenge.setDescription(challengeObject.getString("description"));
                allDailyChallenges.add(challenge);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void addToLayout() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        missions.clear();
        for (Challenge c : challenges) {
            if(!c.isCompleted()){
                missions.add(c);
            }
        }
        ChallengesAdapter adapter = new ChallengesAdapter(getApplicationContext(), missions,this);
        recyclerView.setAdapter(adapter);
    }

    //it had to be done... but at what cost
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refresh(View view){
        RadioButton button = findViewById(R.id.activeButton);
        if(button.isChecked()){
            displayActive(view);
        }else{
            displayCompleted(view);
        }
    }

    public void displayMissions(View view) {
        RadioButton button = findViewById(R.id.activeButton);
        button.setChecked(true);
        isOnMissions = true;
        missions.clear();
        for (Challenge c : challenges) {
                if(!c.isCompleted()){
                    missions.add(c);
                }
        }
        ChallengesAdapter adapter = new ChallengesAdapter(getApplicationContext(), missions,this);
        recyclerView.setAdapter(adapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void displayActive(View view){
        if(isOnMissions){
            displayMissions(view);
        } else{
            displayDailyChallenges2(view);

        }
    }
    public void displayCompleted(View view){
        if(isOnMissions){
            missions.clear();
            for (Challenge c : challenges) {
                    if(c.isCompleted()){
                        missions.add(c);
                    }
            }
            ChallengesAdapter adapter = new ChallengesAdapter(getApplicationContext(), missions,this);
            recyclerView.setAdapter(adapter);
        }
        else{
            completedDailyChallenges.clear();
            for (Challenge c : allDailyChallenges){
                if (c.isCompleted()){
                    currentDailyChallenges.remove(c);
                    completedDailyChallenges.add(c);
                }
            }
            DailyChallengesAdapter adapter = new DailyChallengesAdapter(getApplicationContext(), completedDailyChallenges,this);
            recyclerView.setAdapter(adapter);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void displayDailyChallenges2(View view){
        RadioButton button = findViewById(R.id.activeButton);
        button.setChecked(true);
        isOnMissions = false;
        currentDailyChallenges.clear();
        checkIfNewDate();

        SharedPreferences prefsDateValue = PreferenceManager.getDefaultSharedPreferences(this);
        int currentIndex = prefsDateValue.getInt("dateValue", -1);

        int dailyChallengesPerDay = 3;
        int tmpSizeOfChallenges = dailyChallengesPerDay;

        while(currentDailyChallenges.size()<tmpSizeOfChallenges){
            if (!allDailyChallenges.get(currentIndex).isCompleted()){
                currentDailyChallenges.add(allDailyChallenges.get(currentIndex));
            }
            else{
                tmpSizeOfChallenges--;
            }
            currentIndex++;

        }

        DailyChallengesAdapter adapter = new DailyChallengesAdapter(getApplicationContext(), currentDailyChallenges,this);
        recyclerView.setAdapter(adapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkIfNewDate() {

        ZoneId zoneId = ZoneId.of("Europe/Stockholm");
        LocalDate currentDate = LocalDate.now(zoneId);
        String currentDateString = currentDate.toString();

        SharedPreferences prefsDate = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences prefsDateValue = PreferenceManager.getDefaultSharedPreferences(this);
        /* defValues will only be used the first time the program is ever run, and they will be overwritten immediately with the lines below
           because current date will never be 2000-01-01 */
        int oldValue = prefsDateValue.getInt("dateValue", -1);
        String oldDate = prefsDate.getString("date", "2000-01-01");

        if (!currentDateString.equals(oldDate)) {
            prefsDate.edit().putString("date", currentDateString).apply();

            int newValue = (oldValue+3) % allDailyChallenges.size();
            prefsDateValue.edit().putInt("dateValue", newValue).apply();
        }
    }
    public void goBack(View view){
        this.onBackPressed();
    }
}