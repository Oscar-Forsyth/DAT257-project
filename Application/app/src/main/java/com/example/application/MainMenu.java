package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.application.calendar.ActivitiesActivity;
import com.example.application.challenges.ChallengesActivity;
import com.example.application.recommended.RecommendedActivity;
import com.example.application.sports.AvailableSportsActivity;

public class MainMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    /** INTENTS TO OPEN NEW ACTIVITIES FROM THE MAIN MENU:


  /*  /** Called when the user taps the 'Recommended Sports' button */
    public void openRecommendedSports(View view) {
        //Intent intent = new Intent(this, Wizard.class);
        Intent intent = new Intent(this, RecommendedActivity.class);

        startActivity(intent);
    }

    /** Called when the user taps the 'Upcoming Events' button */
    public void openUpcomingEvents(View view) {
        Intent intent = new Intent(this, ActivitiesActivity.class);

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

}