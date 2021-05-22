package com.example.application;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.application.calendar.EventsActivity;
import com.example.application.challenges.ChallengesActivity;
import com.example.application.recommended.RecommendedActivity;
import com.example.application.sports.AvailableSportsActivity;
import com.example.application.sports.Sport;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView;
    private ImageView backButton;
    private boolean firstTimeInMainMenu;
    private ArrayList<Sport>sports;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = findViewById(R.id.customToolbar);
        textView = (TextView) findViewById(R.id.toolbarText);
        backButton = (ImageView) findViewById(R.id.toolbarBack);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(null);
        getSupportActionBar().setTitle(null);
        textView.setText("Chalmers Sports");
        backButton.setVisibility(View.GONE);
        sports = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("SavedState", MODE_PRIVATE);
        //if there is nothing saved, it is the first time the user opens the app and "firstTimeInMainMenu" should therefore be true
        firstTimeInMainMenu = prefs.getBoolean("firstTimeInMainMenu", true);

        if(firstTimeInMainMenu){
            SharedPreferences.Editor editor = getSharedPreferences("SavedState", MODE_PRIVATE).edit();
            editor.putBoolean("firstTimeInMainMenu", false);
            editor.apply();
            try {
                sports = SportsLoader.extractSportsFromJson(getAssets().open("sports.json"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SportsLoader.saveList(sports, "SavedSportsFile","SavedSportsKey", this);
            for (Sport s : sports){
                System.out.println(s.getName());
            }
        }
    }


    /** INTENTS TO OPEN NEW ACTIVITIES FROM THE MAIN MENU: */


    /** Called when the user taps the 'Recommended Sports' button */
    public void openRecommendedSports(View view) {
        //Intent intent = new Intent(this, Wizard.class);
        Intent intent = new Intent(this, RecommendedActivity.class);

        startActivity(intent);
    }

    /** Called when the user taps the 'Upcoming Events' button */
    public void openUpcomingEvents(View view) {
        Intent intent = new Intent(this, EventsActivity.class);

        startActivity(intent);
    }

    /** Called when the user taps the 'Sports & Committees' button */
    public void openSportsAndCommittees(View view) {
        Intent intent = new Intent(this, AvailableSportsActivity.class);

        startActivity(intent);
    }

    /** Called when the user taps the 'Challenges button' */
    public void openChallenges(View view) {
        Intent intent = new Intent(this, ChallengesActivity.class);

        startActivity(intent);
    }

    //TODO Should not be needed here !?
    public void goBack(View view){
        this.onBackPressed();
    }

    /** Make the back button close the application */
    @Override
    public void onBackPressed(){ moveTaskToBack(true); }

    private void saveList (){

    }
}