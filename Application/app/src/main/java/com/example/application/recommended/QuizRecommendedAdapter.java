package com.example.application.recommended;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.SportsLoader;
import com.example.application.animations.Animations;
import com.example.application.sports.Sport;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This class is responsible for creating(painting) the cards that appear in the recyclerviews in "Your Sports"
 */
public class QuizRecommendedAdapter extends RecyclerView.Adapter<QuizRecommendedAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final boolean isInFavourites;

    private final List<Sport> sports;
    private final List<Sport> favouriteSports;
    private final Context ctx;

    /**
     * the used constructor for this adapter
     * @param ctx its activity
     * @param sports contains all sports that should have a cardview created for them
     * @param isInFavourites true/false depending on which fragment this was called from
     */
    public QuizRecommendedAdapter(Context ctx, List<Sport> sports, boolean isInFavourites){
        this.inflater = LayoutInflater.from(ctx);
        this.ctx=ctx;
        this.sports = sports;
        this.isInFavourites=isInFavourites;
        favouriteSports=SportsLoader.extractSavedSports("SavedFavouritesFile", "SavedFavouritesKey", ctx);

    }
    /**
     * Paints a custom_recommended_sport.xml for each sport. It is called once for every Sport in list sports
     * @return the view of the specified xml
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_recommended_sport, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Modifies the fields for each Sport's xml view, for example sets the xml's Text to the name of the Sport.
     * Also sets listeners to various buttons on the xml
     * @param holder the card (xml)
     * @param position the index in sports that it is currently working on
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(sports.get(position).getName());
        holder.description.setText(sports.get(position).getDescription());
        Picasso.get().load(sports.get(position).getLogo()).resize(300,300).onlyScaleDown().into(holder.logo);
        for(Sport fs : favouriteSports){
           if (fs.getName().equals(sports.get(position).getName())){
               holder.favouriteButton.setChecked(true);
           }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        holder.favouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String nameOfListItem = holder.name.getText().toString();
                int index=0;
                for(int i=0;i<sports.size();i++){
                    if(sports.get(i).getName().equals(nameOfListItem)){
                        index=i;
                        break;
                    }
                }
                Sport sportOfCurrentCard = sports.get(index);
                if(isChecked){
                    favouriteSports.add(sportOfCurrentCard);
                }
                else{
                    removeSportFromFavourites(sportOfCurrentCard);
                    if(isInFavourites){
                        sports.remove(sportOfCurrentCard);
                        removeItemFromRecyclerView(index);
                    }
                }
                SportsLoader.saveList(favouriteSports, "SavedFavouritesFile", "SavedFavouritesKey", ctx);
            }
        });
    }

    private void removeSportFromFavourites(Sport sport){
        int indexOfSportToBeRemoved=-1;
        for (int i=0; i<favouriteSports.size(); i++){
            if(favouriteSports.get(i).getName().equals(sport.getName())){
                indexOfSportToBeRemoved = i;
            }
        }
        if(indexOfSportToBeRemoved!=-1){
            favouriteSports.remove(indexOfSportToBeRemoved);
        }
    }

    private void removeItemFromRecyclerView(int index){
        this.notifyItemRemoved(index);
    }

    /**
     * expands the card and shows more information
     * @param isExpanded if it is already expanded
     * @param v the view that was interacted with
     * @param layoutExpand the hidden parts of the card
     * @return whether expanded or not
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
     * returns the length of the list that holds items to be painted
     * @return the length of the list input
     */
    @Override
    public int getItemCount() {
        return sports.size();
    }
    //---------------------------------------------------------------------------------------

    /**
     * The class that holds the information of the actual view, ie what appears on the screen.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, description;
        Button linkButton;
        ImageView logo, showMore;
        LinearLayout layoutExpand;
        ToggleButton favouriteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);  //Might need to move
            logo = itemView.findViewById(R.id.logo);
            linkButton = itemView.findViewById(R.id.link);
            layoutExpand = itemView.findViewById(R.id.layoutExpand);
            showMore = itemView.findViewById(R.id.showMore);
            favouriteButton = itemView.findViewById(R.id.favouriteButton);


        }
    }
}
