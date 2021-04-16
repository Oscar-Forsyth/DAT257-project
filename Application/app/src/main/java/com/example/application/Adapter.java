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

import com.android.volley.AuthFailureError;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Sport> sports;

    public Adapter(Context ctx, List<Sport> sports){
        this.inflater = LayoutInflater.from(ctx);
        this.sports = sports;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_available_sports_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data
        holder.songTitle.setText(sports.get(position).getName());
        holder.songArtists.setText(sports.get(position).getDescription());
        Picasso.get().load(sports.get(position).getLogo()).into(holder.songCoverImage);

    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView songTitle,songArtists;
        ImageView songCoverImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.name);
            songArtists = itemView.findViewById(R.id.description);
            songCoverImage = itemView.findViewById(R.id.logo);

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