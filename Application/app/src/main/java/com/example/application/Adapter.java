package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Controls the RecyclerView and the adding of content to the RecyclerView.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Sport> sports;


    public Adapter(Context ctx, List<Sport> sports){

        this.inflater = LayoutInflater.from(ctx);
        this.sports = sports;
    }


    /**
     * see RecyclerView.java for more information
     * @param parent
     * @param viewType
     * @return view based on custom_available_sport_layout
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_available_sports_layout,parent,false);
        return new ViewHolder(view);
    }

    /**
     * uses the view created above to access data from our customized card
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data
        holder.name.setText(sports.get(position).getName());
        holder.description.setText(sports.get(position).getDescription());
        Picasso.get().load(sports.get(position).getLogo()).resize(75,75).onlyScaleDown().into(holder.logo);
    }

    /**
     * @return number of items to be displayed in the recyclerView
     */
    @Override
    public int getItemCount() {
        return sports.size();
    }

    /**
     * assigns values to and holds attributes necessary for the items in recyclerView
     */
    public  class ViewHolder extends  RecyclerView.ViewHolder{

        TextView name, description;
        ImageView logo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            logo = itemView.findViewById(R.id.logo);


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

