package com.example.application.challenges;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;

import java.util.ArrayList;
import java.util.List;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ViewHolder>  {
    private final LayoutInflater inflater;
    private final List<Challenge> challenges;
    private final ChallengesActivity parent;
    private List<View> viewList = new ArrayList<>();

    public ChallengesAdapter(Context ctx, List<Challenge> challenges,ChallengesActivity parent){
        this.inflater = LayoutInflater.from(ctx);
        this.challenges = challenges;

        this.parent = parent;

    }

    /**
     * See RecyclerView.java for more information.
     * @param parent
     * @param viewType
     * @return view based on custom_challenges
     */
    @NonNull
    @Override
    public ChallengesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_challenges,parent,false);
        viewList.add(view);
        return new ViewHolder(view);
    }

    /**
     * Adds data to the cards.
     * @param holder
     * @param position
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ChallengesAdapter.ViewHolder holder, int position) {
        String description = challenges.get(position).getDescription();
        holder.title.setText(challenges.get(position).getTitle());
        //holder.startDate.setText(challenges.get(position).getPrettyStartDate());
        holder.endDate.setText(challenges.get(position).getPrettyEndDate());
        holder.location.setText(challenges.get(position).getLocation());
        holder.description.setText(description);
        holder.description.setVisibility(View.GONE);

        addDescriptionListener(holder, position);
        addCheckBoxListener(holder.itemView,position);

        if(challenges.get(position).getLocation().equals(" ")) {
            holder.locationLogo.setVisibility(View.INVISIBLE);
        }

        if(challenges.get(position).isCompleted()){
            CheckBox checkBox = holder.itemView.findViewById(R.id.checkBox);
            checkBox.setChecked(true);
        }

    }
    private void addDescriptionListener(ChallengesAdapter.ViewHolder holder, int position){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.description.getVisibility() == View.GONE && !challenges.get(position).getDescription().equals(" ")) {
                    holder.description.setVisibility(View.VISIBLE);
                    holder.title.setSingleLine(false);
                } else {
                    holder.description.setVisibility(View.GONE);
                    holder.title.setSingleLine(true);
                }


            }

        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addCheckBoxListener(View view, int position){
        CheckBox checkBox = view.findViewById(R.id.checkBox);
        Challenge challenge = challenges.get(position);

        checkBox.setOnClickListener(v -> {
            //animations just dont work on this....

            if(checkBox.isChecked()){
                challenge.setCompleted(true);
               // animateBoxes(view,1);
            }else{
                challenge.setCompleted(false);
                //animateBoxes(view,-1);
            }
            parent.refresh(view);
            parent.saveCompletedMission();


        });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void animateBoxes(View view, int direction){
        CardView cardView = view.findViewById(R.id.cardView);
        TextView deadlineText = view.findViewById(R.id.deadlineText);
        TextView dateText = view.findViewById(R.id.endDate);

        ObjectAnimator animationForBox = ObjectAnimator.ofFloat(cardView, "translationX", direction * 1500f);
        ObjectAnimator animationForDeadlineText = ObjectAnimator.ofFloat(deadlineText, "translationX", direction * 1500f);
        ObjectAnimator animationForDateText = ObjectAnimator.ofFloat(dateText, "translationX", direction * 1500f);

        AnimatorSet animations = new AnimatorSet();
        animations.play(animationForBox).with(animationForDeadlineText);
        animations.play(animationForBox).with(animationForDateText);
        animations.setDuration(550);
        animations.start();




    }


    @Override
    public int getItemCount() {
        return challenges.size();
    }

    /**
     * Assigns values to and holds attributes necessary for the cards in the Challenges tab
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, startDate, endDate, location, description;
        ImageView locationLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            //startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            location = itemView.findViewById(R.id.location);
            locationLogo = itemView.findViewById(R.id.locationLogo);
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
