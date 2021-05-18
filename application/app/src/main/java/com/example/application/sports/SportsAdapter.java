package com.example.application.sports;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.animations.Animations;
import com.example.application.challenges.Challenge;
import com.example.application.challenges.ChallengesActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Controls the RecyclerView and the adding of content to the RecyclerView, such as the functionality and layout design
 * (Those parts that are not done in the custom_available_sports_layout are done here instead)
 */

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.ViewHolder> {
    //TODO make changes that Intellij recommends here
    private LayoutInflater inflater;
    private List<Sport> sports;
    private int mExpandedPosition = -1; //TODO old can be removed
    private AvailableSportsActivity parent;

    public SportsAdapter(Context ctx, List<Sport> sports, AvailableSportsActivity parent) {
        this.inflater = LayoutInflater.from(ctx);
        this.sports = sports;
        this.parent = parent;
    }
    public SportsAdapter(Context ctx, List<Sport> sports) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //This part binds the necessary information to the dynamic TextViews and ImageViews.
        holder.name.setText(sports.get(position).getName());
        holder.description.setText(sports.get(position).getDescription());
        Picasso.get().load(sports.get(position).getLogo()).resize(300,300).onlyScaleDown().into(holder.logo);

        addStarListener(holder.itemView, position);
        addFavouriteCheckBoxListener(holder.itemView, position);

        //When clicking on a sports card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Controls the expanded state
                boolean show = toggleLayout(sports.get(position).isExpanded(), v.findViewById(R.id.showMore), holder.layoutExpand);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addStarListener(View view, int position){
        ToggleButton star = view.findViewById(R.id.button_favorite);
        Sport sport = sports.get(position);

        star.setOnClickListener(v -> {

            if(star.isChecked()){
                sport.setFavourite(true);
                System.out.println("ok");  //TODO: Remove
            }else{
                sport.setFavourite(false);
            }
            parent.saveFavoriteSport();

        });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addFavouriteCheckBoxListener(View view, int position) {
        CheckBox checkBox = view.findViewById(R.id.favoriteCheckBox);
        Sport sport = sports.get(position);

        checkBox.setOnClickListener(v -> {

            if(checkBox.isChecked()){
                sport.setFavourite(true);   //TODO: ???
                parent.displayFavourites(view);
                System.out.println("displaying favorites");  //TODO: Remove
            }else{
                parent.getSportsList();
            }

        });
    }


    /**
     * checks if the box is extended and does the appropriate action depending on what the case is
     * @param isExpanded Boolean that checks if the layout is expanded or not
     * @param v The pressed views "showMore" arrow so that it can be rotated
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
     * Own small class needed for the adapter.
     * Binds values to and holds attributes necessary for the items in recyclerView
     */
    //TODO change to static (Intellij recommendation)
    protected class ViewHolder extends  RecyclerView.ViewHolder{
        TextView name, description;
        Button linkButton;
        ImageView logo, showMore;
        LinearLayout layoutExpand;
        ToggleButton favouriteStar;
        CheckBox showFavourites;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sportName);
            description = itemView.findViewById(R.id.description);  //Might need to move
            logo = itemView.findViewById(R.id.logo);
            linkButton = itemView.findViewById(R.id.link);
            layoutExpand = itemView.findViewById(R.id.layoutExpand);
            showMore = itemView.findViewById(R.id.showMore);
            favouriteStar = itemView.findViewById(R.id.button_favorite);
            showFavourites = itemView.findViewById(R.id.favoriteCheckBox);
        }
    }

}

