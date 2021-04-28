package com.example.application.challenges;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;

import java.util.List;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ViewHolder>  {
    LayoutInflater inflater;
    List<Challenge> activities;

    public ChallengesAdapter(Context ctx, List<Challenge> activities){
        this.inflater = LayoutInflater.from(ctx);
        this.activities = activities;
    }

    /**
     * See RecyclerView.java for more information.
     * @param parent
     * @param viewType
     * @return view based on custom_activities
     */
    @NonNull
    @Override
    public ChallengesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_challenges,parent,false);
        return new ChallengesAdapter.ViewHolder(view);
    }

    /**
     * Adds data to the cards.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ChallengesAdapter.ViewHolder holder, int position) {
        holder.title.setText(activities.get(position).getTitle());
        holder.date.setText(activities.get(position).getPrettyDate());
        holder.location.setText(activities.get(position).getLocation());
    }

    /**
     * Returns number of activities.
     * @return number of activities
     */
    @Override
    public int getItemCount() {
        return activities.size();
    }

    /**
     * Assigns values to and holds attributes necessary for the cards in the Upcoming Events tab
     */
    protected class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, date, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.location);

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
