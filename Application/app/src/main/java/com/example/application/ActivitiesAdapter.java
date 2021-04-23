package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Controls the RecyclerView and the adding of content to the RecyclerView.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<Activity> activities;


    public ActivitiesAdapter(Context ctx, List<Activity> activities){
        this.inflater = LayoutInflater.from(ctx);
        this.activities = activities;
    }


    /**
     * see RecyclerView.java for more information
     * @param parent
     * @param viewType
     * @return view based on custom_available_sport_layout
     */
    @NonNull
    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_activites,parent,false);
        return new ActivitiesAdapter.ViewHolder(view);
    }

    /**
     * uses the view created above to access data from our customized card
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ActivitiesAdapter.ViewHolder holder, int position) {
        // bind the data
        holder.title.setText(activities.get(position).getTitle());
        holder.date.setText(activities.get(position).getPrettyDate());
        holder.location.setText(activities.get(position).getLocation());

    }

    /**
     * @return number of items to be displayed in the recyclerView
     */
    @Override
    public int getItemCount() {
        return activities.size();
    }

    /**
     * assigns values to and holds attributes necessary for the items in recyclerView
     */
    public  class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, date, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.location);

            // handle onClick

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
