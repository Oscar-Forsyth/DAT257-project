package com.example.application.sports;

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
    private List<Sport> favouriteSports;
    private boolean favouriteFilterOn;
    private Context ctx;


    public SportsAdapter(Context ctx, List<Sport> sports, boolean isFavouriteFilterOn){
        this.inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.sports = sports;
        favouriteFilterOn = isFavouriteFilterOn;
        favouriteSports=SportsLoader.extractSavedSports("SavedFavouritesFile", "SavedFavouritesKey", ctx);
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
        //This part binds the necessary information to the dynamic TextViews and ImageViews.
        holder.name.setText(sports.get(position).getName());
        holder.description.setText(sports.get(position).getDescription());
        Picasso.get().load(sports.get(position).getLogo()).resize(300,300).onlyScaleDown().into(holder.logo);

        for(Sport fs : favouriteSports){
            if (fs.getName().equals(sports.get(position).getName())){
                holder.button_favorite.setChecked(true);
            }
        }

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

        holder.button_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                    if(favouriteFilterOn){
                        sports.remove(sportOfCurrentCard);
                        removeItemFromRecyclerView(index);
                    }
                }
                SportsLoader.saveList(favouriteSports, "SavedFavouritesFile", "SavedFavouritesKey", ctx);
                System.out.println("current saved favourites: -------------------------");
                for(Sport s : favouriteSports){
                    System.out.println(s.getName());
                }
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
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
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
        ToggleButton button_favorite;

        LinearLayout layoutExpand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sportName);
            description = itemView.findViewById(R.id.description);  //Might need to move
            logo = itemView.findViewById(R.id.logo);
            linkButton = itemView.findViewById(R.id.link);
            layoutExpand = itemView.findViewById(R.id.layoutExpand);
            showMore = itemView.findViewById(R.id.showMore);
            button_favorite = itemView.findViewById(R.id.button_favorite);
        }
    }

}

