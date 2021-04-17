package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class MainMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /** Called when the user taps the 'Recommended Sports' button */
    /**public void uponRecommendedSports(View)

    /** Called when the user taps the 'Upcoming Events' button */

    /** Called when the user taps the 'Sports & Committees' button */

    /** Called when the user tapts the 'Challenges button' */
}