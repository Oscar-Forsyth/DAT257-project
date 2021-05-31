package com.example.application.recommended;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.application.MainMenu;
import com.example.application.R;

/**
 * The activity that is created when "Your Sports" is clicked in the main menu
 * has an empty layout in the center populated by a fragment, either QuizRecommended or Favourites, dependent on which radiobutton is toggled.
 */
public class RecommendedActivity extends AppCompatActivity {

    /**
     * Checks if the user has taken the quiz previously (TAKEN_QUIZ). If not, one activity is created, and if not, another is instead.
     * @param savedInstanceState last state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("Save", MODE_PRIVATE);
        boolean TAKEN_QUIZ = prefs.getBoolean("takenQuiz", false);

        //if the user has taken the quiz previously, one layout is seen, and if not, another layout that prompts the user to start the quiz is shown
        if(TAKEN_QUIZ){
            setContentView(R.layout.activity_recommended_empty);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.ActivityRecommendedLayout, new QuizRecommended()).commit();
            RadioButton recommendedButton = findViewById(R.id.recommendedButton);
            RadioButton favouritesButton = findViewById(R.id.favouritesButton);
            recommendedButton.setChecked(true);

            //when the user clicks on the Recommended-tab, the QuizRecommended fragment replaces the layout in the middle
            recommendedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.ActivityRecommendedLayout, new QuizRecommended()).commit();
                }
            });
            //when the user clicks on the Favourite-tab, the Favourites fragment replaces the layout in the middle
            favouritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.ActivityRecommendedLayout, new Favourites()).commit();
                }
            });
        } else {
            setContentView(R.layout.activity_recommended);
        }
        TextView textView = findViewById(R.id.toolbarText);
        textView.setText("Your Sports");

    }

    /**
     * opens the quiz activity
     * @param view the current view
     */
    public void openQuizWizard(View view) {
        Intent intent = new Intent(this, Wizard.class);
        startActivity(intent);;
    }

    /**
     * goes back to the previous activity (works just like the back button on the android phone)
     * @param view the current view
     */
    public void goBack(View view){
        Intent intent = new Intent(this,MainMenu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}