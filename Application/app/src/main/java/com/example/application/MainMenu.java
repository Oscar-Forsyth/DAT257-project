package com.example.application;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.application.calendar.ActivitiesActivity;
import com.example.application.recommended.RecommendedActivity;
import com.example.application.sports.AvailableSportsActivity;

public class MainMenu extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView;
    private ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = findViewById(R.id.customToolbar);
        textView = (TextView) findViewById(R.id.toolbarText);
        imageView = (ImageView) findViewById(R.id.toolbarBack);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(null);
        getSupportActionBar().setTitle(null);
        textView.setText("Chalmers Sports");
        imageView.setVisibility(View.GONE);
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

    /** Called when the user taps the 'Upcoming Events' button */
    public void openUpcomingEvents(View view) {
        Intent intent = new Intent(this, ActivitiesActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /** Called when the user taps the 'Sports & Committees' button */
    public void openSportsAndCommittees(View view) {
        Intent intent = new Intent(this, AvailableSportsActivity.class);
        //EditText editText = (EditText) findViewById(R.id.availableSportsActivity);
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

    public void goBack(View view){
        this.onBackPressed();
    }
}