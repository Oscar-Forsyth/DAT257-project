package com.example.application.challenges;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The activity for CIS-Missions and Daily Challenges (the 2 tabs only change the content of the recyclerView)
 */
public class ChallengesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<Challenge> challenges;

    private final List<Challenge> missions = new ArrayList<>();
    //all Daily Challenges specified in the file jsonChallenges that can be found in "assets"-folder
    private final List<Challenge> allDailyChallenges = new ArrayList<>();
    //list of Daily Challenges in Active-tab
    private List<Challenge> currentDailyChallenges = new ArrayList<>();
    //list of Daily Challenges in Completed-tab
    private List<Challenge> completedDailyChallenges = new ArrayList<>();

    private RadioButton activeButton;
    private TextView noChallengeText;

    private boolean isOnMissions = true;
    //specifies how many active daily challenges that will be shown each day
    private final int dailyChallengesPerDay = 3;
    //variables used to store the daily challenge lists (keys accessed by gson)
    private final String completedDailyChallengesKey = "completedDailyChallengesKey";
    private final String completedDailyChallengesJson = "completedDailyChallengesJson";
    private final String currentDailyChallengesKey = "currentDailyChallengesKey";
    private final String currentDailyChallengesJson = "currentDailyChallengesJson";

    //the URL for the google calendar that CIS Missions are gathered from
    //private final static String JSON_URL = "https://www.googleapis.com/calendar/v3/calendars/c6isg5rcllc2ki81mnpnv92g90@group.calendar.google.com/events?key=AIzaSyAfe6owfkgrW0GjN5c3N_DDLELAHagbKEg";
    private final static String JSON_URL = "https://www.googleapis.com/calendar/v3/calendars/c_odn6sl5ek8radngjjs6jst44js@group.calendar.google.com/events?key=AIzaSyAfe6owfkgrW0GjN5c3N_DDLELAHagbKEg";

    /**
     * sets attributes for UI-elements, and loads lists with what was saved from the previous time this Activity was opened
     * @param savedInstanceState
     */
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

        noChallengeText = findViewById(R.id.noChallengeText);
        noChallengeText.setVisibility(View.INVISIBLE);

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

    /**
     * loads saved CIS Missions
     */
    public void extractSavedMissions(){
        SharedPreferences completedMissions = getSharedPreferences("CompletedMissions", MODE_PRIVATE);
        Set<String> set = completedMissions.getStringSet("completedMission",new HashSet<>());
        for (int i = 0; i < set.size(); i++) {
            for (int j = 0; j < missions.size(); j++) {
                if(set.toArray()[i].toString().equals(missions.get(j).getTitle())){
                    missions.get(j).setCompleted(!missions.get(j).isCompleted());
                    break;
                }
            }

        }
    }

    /**
     * saves the CIS Missions in Completed-tab to a HashSet
     */
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
     * saves the Daily Challenges (both the list of Active Daily Challenges and Completed Daily Challenges)
     */
    public void saveDailyChallenges(){
       saveListOfChallenges(currentDailyChallenges, currentDailyChallengesKey, currentDailyChallengesJson);
       saveListOfChallenges(completedDailyChallenges, completedDailyChallengesKey, completedDailyChallengesJson);
    }

    /**
     * saves list of challenges to a specified file (key) and key (key2).
     * @param listOfChallenges the list to be saved
     * @param key  the key to the file where the list is saved
     * @param key2 the key to the actual list
     */
    private void saveListOfChallenges(List<Challenge>listOfChallenges, String key, String key2){
        SharedPreferences.Editor editor = getSharedPreferences(key, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(listOfChallenges);
        editor.putString(key2, json);
        editor.apply();
    }

    /**
     * loads the saved Daily Challenges
     */
    private void extractSavedDailyChallenges(){
        //might not be necessary work with 2 different sharedpreferences. The saved lists can probably be in the same file as long as they are coupled to different keys
        //but unsure whether this would be a good or bad change
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
        //when challenges is pressed for the first time ever, creates default list of daily challenges
        if (json==null){
            currentDailyChallenges = new ArrayList<>();
            completedDailyChallenges = new ArrayList<>();
        }
        currentDailyChallenges.size();

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
        }, error -> Log.d("tag", "onErrorResponse: " + error.getMessage()));
        queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addChallengesFromJSON(JSONObject response) {
        String currentDate = dateToday();
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
                    challenge.setDescription(Html.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                    );
                } catch (JSONException e) {
                    challenge.setDescription(" ");
                }
                if(challenge.getEndDate().compareTo(currentDate) > 0) {
                    challenges.add(challenge);
                }
            }
            missions.addAll(challenges);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * getter for date today
     * @return today's date
     */
    private String dateToday() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return df.format(date);
    }

    /**
     * sorts challenges by due date
     */
    private void sortChallenges() {
        Collections.sort(challenges, (object1, object2) -> object1.getEndDate().compareTo(object2.getEndDate()));


    }
    /**
     * JSON content is read from local file
     */
    private String loadJSONFromAsset()  {
        String json = null;
        try {
            InputStream is = getAssets().open("jsonChallenges.json");
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
     * extracts the daily challenges from jsonChallenges and puts them into the list allDailyChallenges
     */
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

    /**
     * displays the default list (Active CIS Missions)
     */
    private void addToLayout() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        missions.clear();
        for (Challenge c : challenges) {
            if(!c.isCompleted()){
                missions.add(c);
            }
        }
        ChallengesAdapter adapter = new ChallengesAdapter(getApplicationContext(), missions,this);
        updateNoChallengesText("You've completed all active challenges!", adapter.getItemCount() == 0);
        recyclerView.setAdapter(adapter);
    }

    /**
     * depending on the state of the radiogroup with "activeButton" and "completedButton", shows either the list of Active or Completed
     * @param view the current view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refresh(View view){
        if(activeButton.isChecked()){
            displayActive(view);
        }else{
            displayCompleted(view);
        }
    }

    /**
     * displays CIS Missions
     * @param view the current view
     */
    public void displayMissions(View view) {
        isOnMissions = true;
        missions.clear();
        activeButton.setChecked(true);
        for (Challenge c : challenges) {
                if(!c.isCompleted()){
                    missions.add(c);
                }
        }
        ChallengesAdapter adapter = new ChallengesAdapter(getApplicationContext(), missions,this);
        updateNoChallengesText("You've completed all active challenges!", adapter.getItemCount() == 0);
        recyclerView.setAdapter(adapter);
    }
    /**
     * either displays Missions or DailyChallenges (both have Active-tab selected)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void displayActive(View view){
        if(isOnMissions){
            displayMissions(view);
        } else{
            displayCurrentDailyChallenges(view);
        }
    }
    /**
     * either displays Missions or DailyChallenges (both have Completed-tab selected)
     */
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
            updateNoChallengesText("You haven't completed any challenges yet", adapter.getItemCount() == 0);
        }
        else{
            //displays completed daily challenges
            DailyChallengesAdapter adapter = new DailyChallengesAdapter(this);
            recyclerView.setAdapter(adapter);
            updateNoChallengesText("You haven't completed any daily challenges yet", adapter.getItemCount() == 0);
        }
    }

    /**
     * displays a textview if the list of the current view is empty,
     * @param text the textview
     * @param isListEmpty true if list is empty, false otherwise
     */
    private void updateNoChallengesText(String text, boolean isListEmpty) {
        if(isListEmpty) {
            noChallengeText.setText(text);
            noChallengeText.setVisibility(View.VISIBLE);
        } else {
            noChallengeText.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * displays DailyChallenges (Active-tab selected)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void displayCurrentDailyChallenges(View view){
        isOnMissions = false;
        //shows that it is in "Active"-tab at start ("Completed"-tab should not be selected since this method displays Active daily challenges)
        activeButton.setChecked(true);
        //if the date has changed since the last time the app was opened, new daily challenges are presented, else it just loads the saved daily challenges
        if (checkIfNewDate()){
            loadNewCurrentDailyChallenges();
            saveDailyChallenges();
        }
        DailyChallengesAdapter adapter = new DailyChallengesAdapter(this);
        updateNoChallengesText("You've completed all today's challenges!", adapter.getItemCount() == 0);
        recyclerView.setAdapter(adapter);
    }
    /**
     * loads new daily challenges
     */
    private void loadNewCurrentDailyChallenges(){
        currentDailyChallenges.clear();
        SharedPreferences prefsDateValue = PreferenceManager.getDefaultSharedPreferences(this);
        int currentIndex = prefsDateValue.getInt("dateValue", 0);

        for (int i=0; i<dailyChallengesPerDay;i++){
            Challenge c = allDailyChallenges.get(currentIndex%allDailyChallenges.size());
            currentDailyChallenges.add(c);
            currentIndex++;
        }
    }

    /**
     * checks if new date
     * @return whether there has been a change in date or not, since the last time the app was opened
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkIfNewDate() {

        ZoneId zoneId = ZoneId.of("Europe/Stockholm");
        //current date in Sweden
        LocalDate currentDate = LocalDate.now(zoneId);
        String currentDateString = currentDate.toString();

        SharedPreferences prefsDate = PreferenceManager.getDefaultSharedPreferences(this);
        //prefsDateValue is used to separate allDailyChallenges into partitions of size specified by dailyChallengesPerDay
        //example: prefsDateValue==0 --> challenge nr: {0,1,2...dailyChallengesPerDay}  will be the challenges presented that day
        SharedPreferences prefsDateValue = PreferenceManager.getDefaultSharedPreferences(this);
        /* defValues will only be used the first time the program is ever run, and they will be overwritten immediately with the lines below
           because current date will never be 2000-01-01 */
        int oldValue = prefsDateValue.getInt("dateValue", 0);
        String oldDate = prefsDate.getString("date", "2000-01-01");

        if (!currentDateString.equals(oldDate)) {
            prefsDate.edit().putString("date", currentDateString).apply();
            int newValue = (oldValue+dailyChallengesPerDay) % allDailyChallenges.size();
            prefsDateValue.edit().putInt("dateValue", newValue).apply();
            return true;
        }
        return false;
    }
    /**
     * goes back to previous activity
     */
    public void goBack(View view){
        this.onBackPressed();
    }

    /**
     * getter for list that contains Current Daily Challenges
     * @return List of the Daily Challenges in Active-tab
     */
    public List<Challenge> getCurrentDailyChallenges(){
        return currentDailyChallenges;
    }

    /**
     * getter for list that contains Completed Daily Challenges
     * @return list of the Daily Challenges in Completed-tab
     */
    public List<Challenge> getCompletedDailyChallenges(){
        return completedDailyChallenges;
    }

    /**
     * shows whether Active-tab is selected or not
     * @return the state of activeButton
     */
    public boolean isShowingActive(){
        return activeButton.isChecked();
    }

}