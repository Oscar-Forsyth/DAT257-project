package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WizardAdapter extends RecyclerView.Adapter<WizardAdapter.WizardViewHolder> {

    Context context;
    LayoutInflater layoutInflater;

    public WizardAdapter(Context context) {
        this.context = context;
    }

    public String[] wizardQuestions = {
            "This is question 1",
            "This is question 2",
            "This is question 3"
    };

    public String[] wizardRadioButton1 = {
            "option 1, question 1 ",
            "option 2, question 1",
            "option 3, question 1"
    };
    public String[] wizardRadioButton2 = {
            "option 1, question 2 ",
            "option 2, question 2",
            "option 3, question 2"
    };
    public String[] wizardRadioButton3 = {
            "option 1, question 3 ",
            "option 2, question 3",
            "option 3, question 3"
    };
    public String[][] wizardRadioButtons = {
            wizardRadioButton1,
            wizardRadioButton2,
            wizardRadioButton3
    };

    @NonNull
    @Override
    public WizardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //container.removeView((RelativeLayout) object);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.wizard_layout, parent, false);
        return new WizardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WizardViewHolder holder, int position) {
        holder.wizardQuestion.setText(wizardQuestions[position]);
        holder.radioButton1.setText(wizardRadioButtons[position][0]);
        holder.radioButton2.setText(wizardRadioButtons[position][1]);
        holder.radioButton3.setText(wizardRadioButtons[position][2]);

        //container.addView(view);

    }

    @Override
    public int getItemCount() {
        return wizardQuestions.length;
    }


    /**
     * assigns values to and holds attributes necessary for the items in recyclerView
     */
    public class WizardViewHolder extends RecyclerView.ViewHolder {

        TextView wizardQuestion;
        RadioButton radioButton1, radioButton2, radioButton3;


        public WizardViewHolder(@NonNull View itemView) {
            super(itemView);

            wizardQuestion = itemView.findViewById(R.id.wizardQuestion);
            radioButton1 = itemView.findViewById(R.id.wizardRadioButton1);
            radioButton2 = itemView.findViewById(R.id.wizardRadioButton2);
            radioButton3 = itemView.findViewById(R.id.wizardRadioButton3);


            // handle onClick
            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
                }
            });

             */
        }
    }
}














