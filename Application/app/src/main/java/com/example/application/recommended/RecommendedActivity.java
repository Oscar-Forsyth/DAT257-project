package com.example.application.recommended;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.application.R;

public class RecommendedActivity extends AppCompatActivity {

    private boolean TAKEN_QUIZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_recommended);

        SharedPreferences prefs = getSharedPreferences("Save", MODE_PRIVATE);
        TAKEN_QUIZ = prefs.getBoolean("takenQuiz", false);

        if(TAKEN_QUIZ){
            setContentView(R.layout.activity_recommended_empty);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.ActivityRecommendedLayout, new QuizRecommended()).commit();
        } else {
            setContentView(R.layout.activity_recommended);
        }
    }

    public void openQuizWizard(View view) {
        Intent intent = new Intent(this, Wizard.class);
        startActivity(intent);
    }
}