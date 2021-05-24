package com.example.application.challenges;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;

import java.util.List;

//this adapter may not be needed if dailyChallenges' and CIS-missions' xml-layouts are very similar
/**
 * paints Daily Challenges
 */
public class DailyChallengesAdapter extends RecyclerView.Adapter<DailyChallengesAdapter.ViewHolder>  {
    List<Challenge> challenges;
    ChallengesActivity activity;
    LayoutInflater inflater;

    /**
     * constructor for this adapter
     * @param activity the activity within which DailyChallengesAdapter is used
     */
    public DailyChallengesAdapter(ChallengesActivity activity){
        this.inflater = LayoutInflater.from(activity);
        this.activity =activity;
    }

    /**
     * attaches xml-file to view used in this class's ViewHolder
     * @return view based on custom_challenges_daily.xml
     */
    @NonNull
    @Override
    public DailyChallengesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_challenges_daily,parent,false);
        return new ViewHolder(view);
    }

    /**
     * Adds data to the viewable cards.
     * @param holder the ViewHolder, a frame of sorts
     * @param position position of specific card in recyclerview
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
            CheckBox checkBox = holder.itemView.findViewById(R.id.checkBoxDaily);
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

    /**
     * sets listener to checkbox of card
     * moves item between completedDailyChallenges and currentDailyChallenges when checkbox is clicked
     * @param view its view
     * @param position position of specific card in recyclerview
     */
    //unnecessary to refresh the whole list. Using recyclerView.remove etc would be preferable
    private void addCheckBoxListener(View view, int position){
        if (activity.isShowingActive()){
            challenges = activity.getCurrentDailyChallenges();
        }
        else{
            challenges = activity.getCompletedDailyChallenges();
        }
        CheckBox checkBox = view.findViewById(R.id.checkBoxDaily);

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
                activity.refresh(view);

            }
        });

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
    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }

    }
}

