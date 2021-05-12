package com.example.application.recommended;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.application.recommended.RecommendedActivity;
import com.example.application.recommended.WizardAdapter;

import java.util.ArrayList;
import java.util.List;

public class Wizard extends AppCompatActivity {
    private ViewPager2 mSlideViewPager;
    private LinearLayout mDotLayout;
    private WizardAdapter wizardAdapter;

    private Button mWizardNextButton;
    private Button mWizardBackButton;

    private int mCurrentPage;
    private final int NUMBER_OF_QUESTIONS = 6;

    private ArrayList<Integer> resultList = new ArrayList<>();

    View v;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        wizardAdapter = new WizardAdapter(this);

        v = LayoutInflater.from(this).inflate(R.layout.wizard_layout, null);

        mSlideViewPager = findViewById(R.id.slideView);      //TODO: Change the background image (in the XML?)
        mDotLayout = findViewById(R.id.dotsLayout);

        mWizardNextButton = findViewById(R.id.wizardNextButton);
        mWizardBackButton = findViewById(R.id.wizardBackButton);

        mSlideViewPager.setAdapter(wizardAdapter);

        addDotsIndicator(0);

        mSlideViewPager.setCurrentItem(0);
        //mRadioGroup.check(mRadioGroup.getChildAt(0).getId());   //TODO: Remove?

        mSlideViewPager.registerOnPageChangeCallback(viewListener);
        //wizardAdapter.notifyItemChanged(0);


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


    }
    //TODO: Add java-doc
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

            System.out.println("--\n");
            for (int i = 0; i < resultList.size(); i++) {
                System.out.println("Question: " + i + "result: " + resultList.get(i).toString());
            }
            System.out.println("--\n");


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
}