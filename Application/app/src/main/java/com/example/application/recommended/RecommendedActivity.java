package com.example.application.recommended;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.application.MainMenu;
import com.example.application.R;
import com.example.application.SportsLoader;
import com.example.application.sports.Sport;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * The activity that is created when "Your Sports" is clicked in the main menu
 */
public class RecommendedActivity extends AppCompatActivity {

    private boolean TAKEN_QUIZ;
    private Toolbar toolbar;
    private TextView textView;
    private RadioButton recommendedButton;
    private RadioButton favouritesButton;
    private List<Sport> favouriteSports;

    /**
     * Checks if the user has taken the quiz previously (TAKEN_QUIZ). If not, one activity is created, and if not, another is instead.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("Save", MODE_PRIVATE);
        TAKEN_QUIZ = prefs.getBoolean("takenQuiz", false);

        //if the user has taken the quiz previously, one layout is seen, and if not, another layout that prompts the user to start the quiz is shown
        if(TAKEN_QUIZ){
            setContentView(R.layout.activity_recommended_empty);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.ActivityRecommendedLayout, new QuizRecommended()).commit();
            recommendedButton = findViewById(R.id.recommendedButton);
            favouritesButton = findViewById(R.id.favouritesButton);
            recommendedButton.setChecked(true);

            //when the user clicks on the Recommended-tab, the QuizRecommended fragment replaces the layout in the middle
            recommendedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.ActivityRecommendedLayout, new QuizRecommended()).commit();
                    System.out.println("clicked on recommended");
                }
            });
            //when the user clicks on the Favourite-tab, the Favourites fragment replaces the layout in the middle
            favouritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.ActivityRecommendedLayout, new Favourites()).commit();
                    System.out.println("clicked on favourites");
                }
            });
        } else {
            setContentView(R.layout.activity_recommended);
        }
        toolbar = findViewById(R.id.customToolbar);
        textView = (TextView) findViewById(R.id.toolbarText);
        textView.setText("Your Sports");
        //favouriteSports=SportsLoader.extractSavedSports("SavedFavouritesFile", "SavedFavouritesKey", this);

    }

    /**
     * opens the quiz activity
     * @param view
     */
    public void openQuizWizard(View view) {
        Intent intent = new Intent(this, Wizard.class);
        startActivity(intent);;
    }

    /**
     * goes back to the previous activity (works just like the back button on the android phone)
     * @param view
     */

    public void goBack(View view){
        Intent intent = new Intent(this,MainMenu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}