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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.application.recommended.RecommendedActivity;
import com.example.application.recommended.WizardAdapter;

import java.util.ArrayList;

public class Wizard extends AppCompatActivity {
    private ViewPager2 mSlideViewPager;
    private LinearLayout mDotLayout;
    private WizardAdapter wizardAdapter;

    private TextView[] mDots;

    private Button mWizardNextButton;
    private Button mWizardBackButton;

    private int mCurrentPage;
    private final int NUMBER_OF_QUESTIONS = 6;

    private final ArrayList<Integer> resultList = new ArrayList<>(NUMBER_OF_QUESTIONS);

    View v;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        initializeArrayList();

        wizardAdapter = new WizardAdapter(this);

        v = LayoutInflater.from(this).inflate(R.layout.wizard_layout, null);

        mSlideViewPager = findViewById(R.id.slideView);
        mDotLayout = findViewById(R.id.dotsLayout);

        mWizardNextButton = findViewById(R.id.wizardNextButton);
        mWizardBackButton = findViewById(R.id.wizardBackButton);

        mSlideViewPager.setAdapter(wizardAdapter);

        addDotsIndicator(0);

        mSlideViewPager.setCurrentItem(0);
        //mRadioGroup.check(mRadioGroup.getChildAt(0).getId());

        mSlideViewPager.registerOnPageChangeCallback(viewListener);

        mWizardBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });

        mWizardNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(mCurrentPage == NUMBER_OF_QUESTIONS -1 ){
                    //adds the last question to the result list
                    addResultToList();

                    //for loop that checks if any question is 0 (i.e no answered)
                    boolean answeredAllQuestions = true;
                    for (int i = 0; i < resultList.size(); i++) {
                        if(resultList.get(i).equals(0)){
                            answeredAllQuestions = false;
                        }
                    }
                    if(!answeredAllQuestions){
                        Toast.makeText(getApplicationContext(), "Please answer all of the questions ty bruv", Toast.LENGTH_SHORT).show();
                    }else{
                        SharedPreferences.Editor editor = getSharedPreferences("Save", MODE_PRIVATE).edit();
                        editor.putBoolean("takenQuiz", true);
                        editor.putString("savedRecommendations", null);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), RecommendedActivity.class);
                        intent.putExtra("QUIZ_RESULTS", resultList);

                        startActivity(intent);
                    }


                }
                else{
                    mSlideViewPager.setCurrentItem(mCurrentPage+1);
                }

            }
        });


    }

    public void addDotsIndicator(int position){
        mDotLayout.removeAllViews();
        mDots = new TextView[NUMBER_OF_QUESTIONS];


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
            addResultToList();


            System.out.println("--\n");
            for (int i = 0; i < resultList.size(); i++) {
                System.out.println("Question: " + i + "result: " + resultList.get(i).toString());
            }
            System.out.println("--\n");




            addDotsIndicator(position);
            mCurrentPage = position;

            if(position == 0){

                mWizardNextButton.setEnabled(true);
                mWizardBackButton.setEnabled(false);
                mWizardBackButton.setVisibility(View.INVISIBLE);

                mWizardNextButton.setText("Next");
                mWizardBackButton.setText("");
            }else if( position == mDots.length -1){
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

    private void addResultToList(){
        int id= wizardAdapter.getRadioButton(mCurrentPage);
        RadioButton rb = findViewById(id);

        if(id!=-1){
            String result = rb.getText().toString();
            if(mCurrentPage != NUMBER_OF_QUESTIONS -1 ) {


                if (result.equalsIgnoreCase(wizardAdapter.radioButtonGroup1[0])) {
                    resultList.set(mCurrentPage, 1);
                } else if (result.equalsIgnoreCase(wizardAdapter.radioButtonGroup1[1])) {
                    resultList.set(mCurrentPage, 2);
                } else if (result.equalsIgnoreCase(wizardAdapter.radioButtonGroup1[2])) {
                    resultList.set(mCurrentPage, 3);
                }

            }else{
                if (result.equalsIgnoreCase(wizardAdapter.radioButtonGroupLast[0])) {
                    resultList.set(mCurrentPage, 1);
                } else if (result.equalsIgnoreCase(wizardAdapter.radioButtonGroupLast[1])) {
                    resultList.set(mCurrentPage, 2);
                } else if (result.equalsIgnoreCase(wizardAdapter.radioButtonGroupLast[2])) {
                    resultList.set(mCurrentPage, 3);
                }
            }


        }
    }
    private void initializeArrayList(){
        for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
            resultList.add(0);
        }
    }

}