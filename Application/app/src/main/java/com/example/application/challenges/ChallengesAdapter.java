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

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ViewHolder>  {
    LayoutInflater inflater;
    List<Challenge> challenges;

    public ChallengesAdapter(Context ctx, List<Challenge> challenges){
        this.inflater = LayoutInflater.from(ctx);
        this.challenges = challenges;
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
        return new ChallengesAdapter.ViewHolder(view);
    }

    /**
     * Adds data to the cards.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ChallengesAdapter.ViewHolder holder, int position) {
        holder.title.setText(challenges.get(position).getTitle());
        holder.startDate.setText(challenges.get(position).getPrettyStartDate());
        holder.endDate.setText(challenges.get(position).getPrettyEndDate());
        holder.location.setText(challenges.get(position).getLocation());
        if(challenges.get(position).getLocation().equals(" ")) {
                holder.locationLogo.setVisibility(View.INVISIBLE);
        }
        holder.description.setText(challenges.get(position).getDescription());
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

        TextView title, startDate, endDate, location, description;
        ImageView locationLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            startDate = itemView.findViewById(R.id.startDate);
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
