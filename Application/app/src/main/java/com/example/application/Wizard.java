package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
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
import android.widget.RadioGroup;
import android.widget.TextView;

public class Wizard extends AppCompatActivity {
    private ViewPager2 mSlideViewPager;
    private LinearLayout mDotLayout;
    private WizardAdapter wizardAdapter;

    private TextView[] mDots;

    private Button mWizardNextButton;
    private Button mWizardBackButton;

    private int mCurrentPage;

    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioGroup radioGroup;
    private RadioButton selectedRadioButton;

    View v;

    private final int NUMBER_OF_QUESTIONS = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        wizardAdapter = new WizardAdapter(this);

        v = LayoutInflater.from(this).inflate(R.layout.wizard_layout, null);

        radioButton1 = v.findViewById(R.id.radioButton1);
        radioButton2 = v.findViewById(R.id.radioButton2);
        radioButton3 = v.findViewById(R.id.radioButton3);



        mSlideViewPager = findViewById(R.id.slideView);
        mDotLayout = findViewById(R.id.dotsLayout);

        mWizardNextButton = findViewById(R.id.wizardNextButton);
        mWizardBackButton = findViewById(R.id.wizardBackButton);

        mSlideViewPager.setAdapter(wizardAdapter);

        addDotsIndicator(0);

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

                int id= wizardAdapter.getRadioButton(mCurrentPage);
                RadioButton rb = findViewById(id);

                if(id!=-1){
                    System.out.println("text:" + rb.getText().toString());
                }

                if(mCurrentPage == NUMBER_OF_QUESTIONS -1 ){
                    SharedPreferences.Editor editor = getSharedPreferences("Save", MODE_PRIVATE).edit();
                    editor.putBoolean("takenQuiz", true);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), RecommendedActivity.class);
                    startActivity(intent);
                }
                else{
                    mSlideViewPager.setCurrentItem(mCurrentPage+1);
                }

            }
        });
        /*
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton =  group.findViewById(checkedId);
                if(radioButton != null && checkedId != -1){
                    System.out.println(radioButton.getText().toString());
                }
            }
        });

         */

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
            addDotsIndicator(position);
            System.out.print(mCurrentPage +"--");

            mCurrentPage = position;
            System.out.print(mCurrentPage);

            System.out.println("\n");

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

}