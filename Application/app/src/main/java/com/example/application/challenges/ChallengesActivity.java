package com.example.application.challenges;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChallengesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<Challenge> challenges;
    private List<Challenge> missions = new ArrayList<>();
    private List<Challenge> longChallenges = new ArrayList<>();
    //user story 1.8
    private List<Challenge> allDailyChallenges = new ArrayList<>();
    private List<Challenge> currentDailyChallenges = new ArrayList<>();
    private List<Challenge> completedDailyChallenges = new ArrayList<>();
    private RadioButton activeButton;


    private boolean isOnMissions = true;
    private final int dailyChallengesPerDay = 3;
    private final String completedDailyChallengesKey = "completedDailyChallengesKey";
    private final String completedDailyChallengesJson = "completedDailyChallengesJson";
    private final String currentDailyChallengesKey = "currentDailyChallengesKey";
    private final String currentDailyChallengesJson = "currentDailyChallengesJson";


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

        activeButton = findViewById(R.id.activeButton);

        challenges = new ArrayList<>();
        extractChallenges();

        //creates daily challenges based on jsonChallenges
        try {
            extractAllDailyChallenges();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        extractSavedDailyChallenges();
    }
    public void extractSavedMissions(){
        SharedPreferences completedMissions = getSharedPreferences("CompletedMissions", MODE_PRIVATE);
        Set<String> set = completedMissions.getStringSet("completedMission",new HashSet<>());
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

    public void saveDailyChallenges(){
       saveListOfChallenges(currentDailyChallenges, currentDailyChallengesKey, currentDailyChallengesJson);
       saveListOfChallenges(completedDailyChallenges, completedDailyChallengesKey, completedDailyChallengesJson);
    }
    private void saveListOfChallenges(List<Challenge>listOfChallenges, String key, String key2){
        SharedPreferences.Editor editor = getSharedPreferences(key, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(listOfChallenges);
        editor.putString(key2, json);
        editor.apply();
    }

    private void extractSavedDailyChallenges(){
        //TODO might not be necessary to have 2 different sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences(currentDailyChallengesKey, MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences(completedDailyChallengesKey, MODE_PRIVATE);
        Gson gson = new Gson();
        Gson gson2 = new Gson();
        String json = sharedPreferences.getString(currentDailyChallengesJson, null);
        String json2 = sharedPreferences2.getString(completedDailyChallengesJson, null);
        //tells gson to convert the json-file into an arraylist of type Challenge
        Type type = new TypeToken<ArrayList<Challenge>>(){}.getType();
        currentDailyChallenges = gson.fromJson(json,type);
        completedDailyChallenges = gson2.fromJson(json2, type);
        //when the app is run for the first time, creates default list of daily challenges
        if (json==null){
            System.out.println("json is null so lists have to be created");
            currentDailyChallenges = new ArrayList<>();
            for (int i = 0; i<dailyChallengesPerDay; i++){
                currentDailyChallenges.add(allDailyChallenges.get(i));
            }
            completedDailyChallenges = new ArrayList<>();
        }

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
    private void extractAllDailyChallenges() throws JSONException {

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
        activeButton.setChecked(true);
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
            displayCurrentDailyChallenges(view);

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
            //displays completed daily challenges
            DailyChallengesAdapter adapter = new DailyChallengesAdapter(this);
            recyclerView.setAdapter(adapter);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void displayCurrentDailyChallenges(View view){
        isOnMissions = false;
        //shows that it is in "Active"-tab at start ("Completed"-tab should not be selected since this method displays Active daily challenges)
        activeButton.setChecked(true);
        //if the date has changed since the last time the app was opened, new daily challenges are presented, else it just loads the saved daily challenges
        if (checkIfNewDate()){
            loadNewCurrentDailyChallenges();
        }
        DailyChallengesAdapter adapter = new DailyChallengesAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void loadNewCurrentDailyChallenges(){
        currentDailyChallenges.clear();
        SharedPreferences prefsDateValue = PreferenceManager.getDefaultSharedPreferences(this);
        int currentIndex = prefsDateValue.getInt("dateValue", -1);

        for (int i=0; i<dailyChallengesPerDay;i++){
            Challenge c = allDailyChallenges.get(currentIndex%allDailyChallenges.size());
            currentDailyChallenges.add(c);
            currentIndex++;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkIfNewDate() {

        ZoneId zoneId = ZoneId.of("Europe/Stockholm");
        LocalDate currentDate = LocalDate.now(zoneId);
        String currentDateString = currentDate.toString();

        SharedPreferences prefsDate = PreferenceManager.getDefaultSharedPreferences(this);
        //prefsDateValue is used to separate allDailyChallenges into partitions of size specified by dailyChallengesPerDay
        SharedPreferences prefsDateValue = PreferenceManager.getDefaultSharedPreferences(this);
        /* defValues will only be used the first time the program is ever run, and they will be overwritten immediately with the lines below
           because current date will never be 2000-01-01 */
        int oldValue = prefsDateValue.getInt("dateValue", -1);
        String oldDate = prefsDate.getString("date", "2000-01-01");

        if (!currentDateString.equals(oldDate)) {

            prefsDate.edit().putString("date", currentDateString).apply();

            int newValue = (oldValue+dailyChallengesPerDay) % allDailyChallenges.size();
            prefsDateValue.edit().putInt("dateValue", newValue).apply();
            return true;
        }
        return false;
    }
    public void goBack(View view){
        this.onBackPressed();
    }

    public List<Challenge> getCurrentDailyChallenges(){
        return currentDailyChallenges;
    }
    public List<Challenge> getCompletedDailyChallenges(){
        return completedDailyChallenges;
    }
    public boolean isShowingActive(){
        return activeButton.isChecked();
    }
    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

}