package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Wizard extends AppCompatActivity {
    private ViewPager2 mSlideViewPager;
    private LinearLayout mDotLayout;
    private WizardAdapter wizardAdapter;

    private TextView[] mDots;

    private Button mWizardNextButton;
    private Button mWizardBackButton;

    private int mCurrentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        mSlideViewPager = (ViewPager2) findViewById(R.id.slideView);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        mWizardNextButton = (Button) findViewById(R.id.wizardNextButton);
        mWizardBackButton = (Button) findViewById(R.id.wizardBackButton);

        wizardAdapter = new WizardAdapter(this);

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
                mSlideViewPager.setCurrentItem(mCurrentPage+1);
            }
        });
    }

    public void addDotsIndicator(int position){
        mDotLayout.removeAllViews();
        mDots = new TextView[3];

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.white));

            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.black));
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