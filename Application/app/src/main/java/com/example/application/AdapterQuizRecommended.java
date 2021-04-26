package com.example.application;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.animations.Animations;
import com.example.application.sports.Sport;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterQuizRecommended extends RecyclerView.Adapter<AdapterQuizRecommended.ViewHolder>{

    private LayoutInflater inflater;

    private List<Sport> sports;
    private int mExpandedPosition = -1;

    public AdapterQuizRecommended(Context ctx, List<Sport> sports){
        this.inflater = LayoutInflater.from(ctx);
        this.sports = sports;

    }
    //super methods---------------------------------------------------------------------------------------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_recommended_sport, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(sports.get(position).getName());
        holder.description.setText(sports.get(position).getDescription());
        Picasso.get().load(sports.get(position).getLogo()).resize(75,75).onlyScaleDown().into(holder.logo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean show = toggleLayout(!sports.get(position).isExpanded(), v.findViewById(R.id.showMore), holder.layoutExpand);
                sports.get(position).setExpanded(show);
            }
        });

        //Onclick for website button
        holder.linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = sports.get(position).getLink();
                System.out.println(url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });
    }
    private boolean toggleLayout(boolean isExpanded, View v, LinearLayout layoutExpand) {
        Animations.toggleArrow(v, isExpanded);
        if (isExpanded) {
            Animations.expand(layoutExpand);
        } else {
            Animations.collapse(layoutExpand);
        }
        return isExpanded;
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }
    //---------------------------------------------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, description;
        Button linkButton;
        ImageView logo, showMore;
        LinearLayout layoutExpand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);  //Might need to move
            logo = itemView.findViewById(R.id.logo);
            linkButton = itemView.findViewById(R.id.link);
            layoutExpand = itemView.findViewById(R.id.layoutExpand);
            showMore = itemView.findViewById(R.id.showMore);
        }
    }
}
