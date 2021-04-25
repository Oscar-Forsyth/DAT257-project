package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame, new NotTakenQuiz()).commit();
    }
}