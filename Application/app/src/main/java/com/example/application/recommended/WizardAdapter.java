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
            "Do you prefer to play sports indoors or outdoors?",
            "Do you prefer to play sports in a group or individually?",
            "What type of of performance level do you prefer?",
            "If you had to choose a category of sports, which would it be?"
    };

    public String[] radioButtonGroup1 = {
            "Indoors",
            "Outdoors",
            "No preference"
    };
    public String[] radioButtonGroup2 = {
            "In groups",
            "Individually",
            "No preference"
    };
    public String[] radioButtonGroup3 = {
            "Intensive",
            "Endurance",
            "Both"
    };
    public String[] radioButtonGroup4 = {
            "Ball sports",
            "Racket sports",
            "Precision sports",
            "Sports that are close to nature",
            "Sports with complex movements"
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
    //TODO Change this method to fill with each question.
    public void onBindViewHolder(@NonNull WizardViewHolder holder, int position) {
        holder.wizardQuestion.setText(wizardQuestions[position]);
        holder.radioButton4.setVisibility(View.GONE);
        holder.radioButton5.setVisibility(View.GONE);
        if(position == 0){
            holder.radioButton1.setText(radioButtonGroup1[0]);
            holder.radioButton2.setText(radioButtonGroup1[1]);
            holder.radioButton3.setText(radioButtonGroup1[2]);

        }else if(position == 1){
            holder.radioButton1.setText(radioButtonGroup2[0]);
            holder.radioButton2.setText(radioButtonGroup2[1]);
            holder.radioButton3.setText(radioButtonGroup2[2]);
        }
        else if(position == 2){
            holder.radioButton1.setText(radioButtonGroup3[0]);
            holder.radioButton2.setText(radioButtonGroup3[1]);
            holder.radioButton3.setText(radioButtonGroup3[2]);
        }
        else if(position == 3){
            holder.radioButton4.setVisibility(View.VISIBLE);
            holder.radioButton5.setVisibility(View.VISIBLE);
            holder.radioButton1.setText(radioButtonGroup4[0]);
            holder.radioButton2.setText(radioButtonGroup4[1]);
            holder.radioButton3.setText(radioButtonGroup4[2]);
            holder.radioButton4.setText(radioButtonGroup4[3]);
            holder.radioButton5.setText(radioButtonGroup4[4]);
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
            if(position == 0){
                if (result.equalsIgnoreCase(radioButtonGroup1[0])) {
                    quizResults.set(position, 1);
                } else if (result.equalsIgnoreCase(radioButtonGroup1[1])) {
                    quizResults.set(position, 2);
                } else if (result.equalsIgnoreCase(radioButtonGroup1[2])) {
                    quizResults.set(position, 3);
                }
            }
            else if(position == 1){
                if (result.equalsIgnoreCase(radioButtonGroup2[0])) {
                    quizResults.set(position, 1);
                } else if (result.equalsIgnoreCase(radioButtonGroup2[1])) {
                    quizResults.set(position, 2);
                } else if (result.equalsIgnoreCase(radioButtonGroup2[2])) {
                    quizResults.set(position, 3);
                }
            }
            else if(position == 2){
                if (result.equalsIgnoreCase(radioButtonGroup3[0])) {
                    quizResults.set(position, 1);
                } else if (result.equalsIgnoreCase(radioButtonGroup3[1])) {
                    quizResults.set(position, 2);
                } else if (result.equalsIgnoreCase(radioButtonGroup3[2])) {
                    quizResults.set(position, 3);
                }
            }else{ //Sista frågan
                if (result.equalsIgnoreCase(radioButtonGroup4[0])) {
                    quizResults.set(position, 1);
                } else if (result.equalsIgnoreCase(radioButtonGroup4[1])) {
                    quizResults.set(position, 2);
                } else if (result.equalsIgnoreCase(radioButtonGroup4[2])) {
                    quizResults.set(position, 3);
                } else if (result.equalsIgnoreCase(radioButtonGroup4[3])) {
                    quizResults.set(position, 4);
                } else if (result.equalsIgnoreCase(radioButtonGroup4[4])) {
                    quizResults.set(position, 5);
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
        RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;
        RadioGroup radioGroup;


        public WizardViewHolder(@NonNull View itemView) {
            super(itemView);

            wizardQuestion = itemView.findViewById(R.id.wizardQuestion);
            radioButton1 = itemView.findViewById(R.id.radioButton1);
            radioButton2 = itemView.findViewById(R.id.radioButton2);
            radioButton3 = itemView.findViewById(R.id.radioButton3);
            radioButton4 = itemView.findViewById(R.id.radioButton4);
            radioButton5 = itemView.findViewById(R.id.radioButton5);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            //radioGroup.check(radioGroup.getChildAt(0).getId());
           // radioGroup.clearCheck();

        }

        public RadioGroup getRadioGroup() {
            return radioGroup;
        }
    }
}














