package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

public class WizardAdapter extends RecyclerView.Adapter<WizardAdapter.WizardViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    RadioGroup radioGroup;
    View view;
    Map<Integer, WizardViewHolder> map = new HashMap<>();
    int count= 0;

    public WizardAdapter(Context context) {
        this.context = context;
    }

    public String[] wizardQuestions = {
            "This is question 1",
            "This is question 2",
            "This is question 3"
    };

    public String[] radioButtonGroup1 = {
            "option 1, question 1 ",
            "option 1, question 2",
            "option 1, question 3"
    };
    public String[] radioButtonGroup2 = {
            "option 2, question 1 ",
            "option 2, question 2",
            "option 2, question 3"
    };
    public String[] radioButtonGroup3 = {
            "option 3, question 1 ",
            "option 3, question 2",
            "option 3, question 3"
    };
    public String[][] wizardRadioButtons = {
            radioButtonGroup1,
            radioButtonGroup2,
            radioButtonGroup3
    };

    public int getRadioButton(int key){
        WizardViewHolder wizardViewHolder = map.get(key);
        System.out.println(map.toString());
        RadioGroup radioGroup = wizardViewHolder.getRadioGroup();
        int result = radioGroup.getCheckedRadioButtonId();
        /*
        RadioButton selectedRadioButton;
            if(result != -1){
                selectedRadioButton = view.findViewById(result);
                String RBText = selectedRadioButton.getText().toString();
                System.out.println(RBText);
            }else {
                System.out.println("Nothing selected");
            }


         */
        return result;
    }


    @NonNull
    @Override
    public WizardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //container.removeView((RelativeLayout) object);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.wizard_layout, parent, false);
        WizardViewHolder wizardViewHolder = new WizardViewHolder(view);
        map.put(count,wizardViewHolder);
        count++;
        return wizardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WizardViewHolder holder, int position) {

        view = holder.itemView;
        holder.wizardQuestion.setText(wizardQuestions[position]);
        holder.radioButton1.setText(wizardRadioButtons[0][position]);
        holder.radioButton2.setText(wizardRadioButtons[1][position]);
        holder.radioButton3.setText(wizardRadioButtons[2][position]);
    }

    @Override
    public int getItemCount() {
        return wizardQuestions.length;
    }


    /**
     * assigns values to and holds attributes necessary for the items in recyclerView
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

        }

        public RadioGroup getRadioGroup() {
            return radioGroup;
        }
    }
}














