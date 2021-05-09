package com.example.application.challenges;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;

import java.util.List;
//this adapter may not be needed if dailyChallenges' and CIS-missions' xml-layouts are very similar
public class DailyChallengesAdapter extends RecyclerView.Adapter<DailyChallengesAdapter.ViewHolder>  {
    LayoutInflater inflater;
    List<Challenge> challenges;

    ChallengesActivity activity;

    public DailyChallengesAdapter(ChallengesActivity activity){
        this.inflater = LayoutInflater.from(activity);

        this.activity=activity;

    }

    /**
     * See RecyclerView.java for more information.
     * @param parent
     * @param viewType
     * @return view based on custom_challenges
     */

    @NonNull
    @Override
    public DailyChallengesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_challenges_daily,parent,false);
        return new DailyChallengesAdapter.ViewHolder(view);
    }

    /**
     * Adds data to the cards.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull DailyChallengesAdapter.ViewHolder holder, int position) {
        if (activity.isShowingActive()){
            challenges = activity.getCurrentDailyChallenges();
        }
        else{
            challenges = activity.getCompletedDailyChallenges();
        }
        holder.title.setText(challenges.get(position).getTitle());
        holder.description.setText(challenges.get(position).getDescription());
        holder.description.setVisibility(View.GONE);
        addCheckBoxListener(holder.itemView,position);
        //if in "Completed"-tab every challenge should have its checkbox checked
        if(challenges.get(position).isCompleted()){
            CheckBox checkBox = holder.itemView.findViewById(R.id.checkBox);
            checkBox.setChecked(true);
        }
        //shows challenge's description on click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.description.getVisibility() == View.GONE) {
                    holder.description.setVisibility(View.VISIBLE);
                } else {
                    holder.description.setVisibility(View.GONE);
                }
            }
        });
    }

//moves item between completedDailyChallenges and currentDailyChallenges when checkbox is clicked
    private void addCheckBoxListener(View view, int position){
        if (activity.isShowingActive()){
            challenges = activity.getCurrentDailyChallenges();
        }
        else{
            challenges = activity.getCompletedDailyChallenges();
        }
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        Challenge challenge = challenges.get(position);
        checkBox.setOnClickListener( new View.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){

                    challenge.setCompleted(true);
                    activity.getCompletedDailyChallenges().add(challenge);

                }else{

                    challenge.setCompleted(false);
                    activity.getCurrentDailyChallenges().add(challenge);

                }
                challenges.remove(challenge);
                activity.saveDailyChallenges();
                animateBox(view,-1);
                activity.refresh(view);

            }
        });

    }


    private void animateBox(View view,int direction){
        CardView cardView = view.findViewById(R.id.cardView);

        ObjectAnimator animationForBox = ObjectAnimator.ofFloat(cardView, "translationX", direction * 1500f);

        AnimatorSet animations = new AnimatorSet();
        animations.play(animationForBox).with(animationForBox);

        animations.setDuration(850);
        animations.start();
    }

    /**
     * Returns number of challenges.
     * @return number of challenges
     */
    @Override
    public int getItemCount() {
        if(activity.isShowingActive()){
            return activity.getCurrentDailyChallenges().size();
        }
        else{
            return activity.getCompletedDailyChallenges().size();
        }
    }

    /**
     * Assigns values to and holds attributes necessary for the cards in the Challenges tab
     */
    protected class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

            addListener(itemView);
        }

        private void addListener(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

