package com.example.application.sports;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.animations.Animations;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Controls the RecyclerView and the adding of content to the RecyclerView, such as the functionality and layout design
 * (Those parts that are not done in the custom_available_sports_layout are done here instead)
 */

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.ViewHolder> {
    private LayoutInflater inflater;

    private List<Sport> sports;
    private int mExpandedPosition = -1;

    public SportsAdapter(Context ctx, List<Sport> sports){
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
     * Binds the data from the JSON file that was extracted earlier to the view that was created earlier.
     * Also sets different OnClick features
     * @param holder
     * @param position
     */

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data

        holder.name.setText(sports.get(position).getName());
        holder.description.setText(sports.get(position).getDescription());
        Picasso.get().load(sports.get(position).getLogo()).resize(150,150).onlyScaleDown().into(holder.logo);

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

    /**
     * checks if the box is extended and does the appropriate action depending on what the case is
     * @param isExpanded Boolean that checks if the layout is expanded or not
     * @param v The pressed views "showMore" so that it can be rotated
     * @param layoutExpand The layout that needs to expand
     * @return
     */





    private boolean toggleLayout(boolean isExpanded, View v, LinearLayout layoutExpand) {
        Animations.toggleArrow(v, isExpanded);
        if (isExpanded) {
            Animations.expand(layoutExpand);
        } else {
            Animations.collapse(layoutExpand);
        }
        return isExpanded;

    }


    /**
     * @return number of items to be displayed in the recyclerView
     */
    @Override
    public int getItemCount() {
        return sports.size();
    }

    /**
     * Binds values to and holds attributes necessary for the items in recyclerView
     */
    protected class ViewHolder extends  RecyclerView.ViewHolder{

        TextView name, description;
        Button linkButton;
        ImageView logo, showMore;
        LinearLayout layoutExpand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.sportName);
            description = itemView.findViewById(R.id.description);  //Might need to move
            logo = itemView.findViewById(R.id.logo);
            linkButton = itemView.findViewById(R.id.link);
            layoutExpand = itemView.findViewById(R.id.layoutExpand);
            showMore = itemView.findViewById(R.id.showMore);

        }
    }




}

