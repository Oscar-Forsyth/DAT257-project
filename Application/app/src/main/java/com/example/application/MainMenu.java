package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    /** INTENTS TO OPEN NEW ACTIVITIES FROM THE MAIN MENU:


  /*  /** Called when the user taps the 'Recommended Sports' button */
    public void openRecommendedSports(View view) {
        //Intent intent = new Intent(this, Wizard.class);
        Intent intent = new Intent(this, RecommendedActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /** Called when the user taps the 'Upcoming Events' button *//*
    public void openUpcomingEvents(View view) {
        Intent intent = new Intent(this, UpcomingEvents.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    *//** Called when the user taps the 'Sports & Committees' button */
    public void openSportsAndCommittees(View view) {
        Intent intent = new Intent(this, AvailableSportsActivity.class);
        EditText editText = (EditText) findViewById(R.id.availableSportsActivity);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /** Called when the user taps the 'Challenges button' */
    public void openChallenges(View view) {
        //Intent intent = new Intent(this, QuizActivity.class);

        //startActivity(intent);
        /*
        Intent intent = new Intent(this, Challenges.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

         */
    }

}