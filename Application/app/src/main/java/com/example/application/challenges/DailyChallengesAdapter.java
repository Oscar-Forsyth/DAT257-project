package com.example.application.challenges;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;

import java.util.List;

public class DailyChallengesAdapter extends RecyclerView.Adapter<DailyChallengesAdapter.ViewHolder>  {
    LayoutInflater inflater;
    List<Challenge> challenges;

    public DailyChallengesAdapter(Context ctx, List<Challenge> challenges){
        this.inflater = LayoutInflater.from(ctx);
        this.challenges = challenges;
    }

    /**
     * See RecyclerView.java for more information.
     * @param parent
     * @param viewType
     * @return view based on custom_challenges
     */
    //TODO should have another custom xml layout that does not hold variables such as date and location
    @NonNull
    @Override
    public DailyChallengesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_challenges,parent,false);
        return new DailyChallengesAdapter.ViewHolder(view);
    }

    /**
     * Adds data to the cards.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull DailyChallengesAdapter.ViewHolder holder, int position) {
        holder.title.setText(challenges.get(position).getTitle());
        holder.description.setText(challenges.get(position).getDescription());
        holder.description.setVisibility(View.GONE);

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
     * Returns number of challenges.
     * @return number of challenges
     */
    @Override
    public int getItemCount() {
        return challenges.size();
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

