package com.example.application.calendar;

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

/**
 * Controls the information that should be visible on the cards in the Upcoming Event tab.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<Activity> activities;

    public ActivitiesAdapter(Context ctx, List<Activity> activities){
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
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_activites,parent,false);
        return new ActivitiesAdapter.ViewHolder(view);
    }

    /**
     * Adds data to the cards.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ActivitiesAdapter.ViewHolder holder, int position) {
        holder.title.setText(activities.get(position).getTitle());
        holder.date.setText(activities.get(position).getPrettyDate());
        holder.location.setText(activities.get(position).getLocation());
        holder.description.setText(activities.get(position).getDescription());
        holder.description.setVisibility(View.GONE);
        if(activities.get(position).getDescription().equals(" ")) {
            holder.descriptionLogo.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.description.getVisibility() == View.GONE && !activities.get(position).getDescription().equals(" ")) {
                    holder.description.setVisibility(View.VISIBLE);
                    holder.descriptionLogo.animate().setDuration(100).rotation(180);

                } else {
                    holder.description.setVisibility(View.GONE);
                    holder.descriptionLogo.animate().setDuration(100).rotation(0);
                }

            }
        });
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

        TextView title, date, location, description;
        ImageView descriptionLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.location);
            description = itemView.findViewById(R.id.description);
            descriptionLogo = itemView.findViewById(R.id.descriptionLogo);

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
