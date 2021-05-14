package com.example.application.recommended;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;

import java.util.ArrayList;

public class WizardAdapter extends RecyclerView.Adapter<WizardAdapter.WizardViewHolder> {

    Context context;
    LayoutInflater layoutInflater;

    private final ArrayList<Integer> quizResults = new ArrayList<>();

    public WizardAdapter(Context context) {
        this.context = context;
        initializeArrayList();
    }

    public String[] wizardQuestions = {
            "I like to work with others towards a common goal",
            "I like playing sports outside",
            "I like longer training sessions rather than short and intense",
            "I like to move with precision and coordination",
            "I like water sports",
            "Which sport sounds most interesting?"
    };

    public String[] radioButtonGroup1 = {  //TODO: Change to radioButtonGroupFirst to be more consistent with the other group?
            "Yes",
            "No",
            "Sometimes"
    };
    public String[] radioButtonGroupLast = {      //TODO: Change to a multi-choice question
            "Ball sports",
            "Sports played with rackets",         //TODO: Change to Racket sports
            "Extreme sports (i.e snowboarding, mountain climbing)"
    };



    public ArrayList<Integer> getQuizResults() {
        return quizResults;
    }

    private void initializeArrayList(){
        for (int i = 0; i < getItemCount(); i++) {
            quizResults.add(0);
        }

    }


    @NonNull
    @Override
    public WizardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.wizard_layout, parent, false);
        return new WizardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WizardViewHolder holder, int position) {
        holder.wizardQuestion.setText(wizardQuestions[position]);
        if(position <5){
            holder.radioButton1.setText(radioButtonGroup1[0]);
            holder.radioButton2.setText(radioButtonGroup1[1]);
            holder.radioButton3.setText(radioButtonGroup1[2]);
        }else{
            holder.radioButton1.setText(radioButtonGroupLast[0]);
            holder.radioButton2.setText(radioButtonGroupLast[1]);
            holder.radioButton3.setText(radioButtonGroupLast[2]);
        }

        RadioGroup radioGroup = holder.getRadioGroup();
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = holder.itemView.findViewById(checkedId);
            setButtonResult(checkedId,radioButton,position);
        });
    }
    private void setButtonResult(int id,RadioButton radioButton,int position){
        if(id!=-1){
            String result = radioButton.getText().toString();
            if(position != getItemCount() -1 ) {
                if (result.equalsIgnoreCase(radioButtonGroup1[0])) {
                    quizResults.set(position, 1);
                } else if (result.equalsIgnoreCase(radioButtonGroup1[1])) {
                    quizResults.set(position, 2);
                } else if (result.equalsIgnoreCase(radioButtonGroup1[2])) {
                    quizResults.set(position, 3);
                }
            }else{
                if (result.equalsIgnoreCase(radioButtonGroupLast[0])) {
                    quizResults.set(position, 1);
                } else if (result.equalsIgnoreCase(radioButtonGroupLast[1])) {
                    quizResults.set(position, 2);
                } else if (result.equalsIgnoreCase(radioButtonGroupLast[2])) {
                    quizResults.set(position, 3);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return wizardQuestions.length;
    }


    /**
     * Assigns values to and holds attributes necessary for the items in recyclerView
     */
    public static class WizardViewHolder extends RecyclerView.ViewHolder {

        TextView wizardQuestion;
        RadioButton radioButton1, radioButton2, radioButton3;
        RadioGroup radioGroup;


        public WizardViewHolder(@NonNull View itemView) {
            super(itemView);

            wizardQuestion = itemView.findViewById(R.id.wizardQuestion);
            radioButton1 = itemView.findViewById(R.id.radioButton1);
            radioButton2 = itemView.findViewById(R.id.radioButton2);
            radioButton3 = itemView.findViewById(R.id.radioButton3);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            //radioGroup.check(radioGroup.getChildAt(0).getId());
           // radioGroup.clearCheck();

        }

        public RadioGroup getRadioGroup() {
            return radioGroup;
        }
    }
}














