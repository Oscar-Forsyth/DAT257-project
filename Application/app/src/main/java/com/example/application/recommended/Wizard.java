package com.example.application.recommended;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.example.application.MainMenu;
import com.example.application.R;

import java.util.ArrayList;

/**
 * responsible for the view and functionality of the quiz
 */
public class Wizard extends AppCompatActivity {
    private ViewPager2 mSlideViewPager;
    private LinearLayout mDotLayout;
    private WizardAdapter wizardAdapter;

    private Button mWizardNextButton;
    private Button mWizardBackButton;

    private int mCurrentPage;
    private final int NUMBER_OF_QUESTIONS = 4;

    private ArrayList<Integer> resultList = new ArrayList<>();

    View v;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        wizardAdapter = new WizardAdapter(this);

        v = LayoutInflater.from(this).inflate(R.layout.wizard_layout, null);

        mSlideViewPager = findViewById(R.id.slideView);
        mDotLayout = findViewById(R.id.dotsLayout);

        mWizardNextButton = findViewById(R.id.wizardNextButton);
        mWizardBackButton = findViewById(R.id.wizardBackButton);

        mSlideViewPager.setAdapter(wizardAdapter);

        addDotsIndicator(0);

        //Stupid bug that forces these method calls, due to dotLayout nor appearing
        mSlideViewPager.setCurrentItem(1);
        mSlideViewPager.setCurrentItem(0);
        viewListener.onPageSelected(0);

        mSlideViewPager.registerOnPageChangeCallback(viewListener);

        mWizardBackButton.setOnClickListener(view -> mSlideViewPager.setCurrentItem(mCurrentPage-1));

        mWizardNextButton.setOnClickListener(view -> {


            if(mCurrentPage == NUMBER_OF_QUESTIONS -1 ){
                //adds the last question to the result list
                resultList = wizardAdapter.getQuizResults();

                //for loop that checks if any question is 0 (i.e no answered)
                boolean answeredAllQuestions = true;
                for (int i = 0; i < resultList.size(); i++) {
                    if(resultList.get(i).equals(0)){
                        answeredAllQuestions = false;
                    }
                }
                if(!answeredAllQuestions){
                    Toast.makeText(getApplicationContext(), "Please answer all of the questions", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor = getSharedPreferences("Save", MODE_PRIVATE).edit();
                    editor.putBoolean("takenQuiz", true);
                    editor.putString("savedRecommendations", null);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), RecommendedActivity.class);
                    intent.putExtra("QUIZ_RESULTS",  resultList);

                    startActivity(intent);
                }


            }
            else{
                mSlideViewPager.setCurrentItem(mCurrentPage+1);
            }

        });

        //Set toolbar text
        TextView toolbarText = findViewById(R.id.toolbarText);
        toolbarText.setText("Quiz");
    }



    /** Add navigation dots to show user which page it is on*/
    public void addDotsIndicator(int position){
        mDotLayout.removeAllViews();
        TextView[] mDots = new TextView[NUMBER_OF_QUESTIONS];
        
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.darker_white));

            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager2.OnPageChangeCallback viewListener = new ViewPager2.OnPageChangeCallback(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;
            resultList = wizardAdapter.getQuizResults();


            if(position == 0){
                mWizardNextButton.setEnabled(true);
                mWizardBackButton.setEnabled(false);
                mWizardBackButton.setVisibility(View.INVISIBLE);

                mWizardNextButton.setText("Next");
                mWizardBackButton.setText("");
            }else if( position == NUMBER_OF_QUESTIONS -1){
                mWizardNextButton.setEnabled(true);
                mWizardBackButton.setEnabled(true);
                mWizardBackButton.setVisibility(View.VISIBLE);

                mWizardNextButton.setText("Finish");
                mWizardBackButton.setText("Back");
            } else{
                mWizardNextButton.setEnabled(true);
                mWizardBackButton.setEnabled(true);
                mWizardBackButton.setVisibility(View.VISIBLE);

                mWizardNextButton.setText("Next");
                mWizardBackButton.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /** Called when the back arrow is pressed in the toolbar */
    public void goBack(View view){
        Intent intent = new Intent(this,MainMenu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}