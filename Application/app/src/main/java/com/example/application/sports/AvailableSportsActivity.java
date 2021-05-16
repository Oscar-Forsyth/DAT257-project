package com.example.application.sports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.FrameLayout;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.application.R;
import com.example.application.Tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class more or less extracts the information that's stored in sports.json and puts it into a
 * Arraylist so that it can be used later. It also serves as the base for a new activity
 */
public class AvailableSportsActivity extends AppCompatActivity {
    /**
     * the framework of the list displaying the sports
     */
    private RecyclerView recyclerView;
    private FrameLayout backgroundFilter;
    protected List<Sport> sports;
    private List<Tag> savedTags;

    /**
     * the URL for our JSON-file
     * For every update to the JSON-file, a new URL has to be generated so there is probably a better solution
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_sports);

        TextView textView = findViewById(R.id.toolbarText);
        textView.setText("Sports and Committees");


        recyclerView = findViewById(R.id.sportsList);
        backgroundFilter = findViewById(R.id.backgroundFilter);
        backgroundFilter.setAlpha((float)(0.6)); //TODO Move to xml

        sports = new ArrayList<>();

        try {
            extractSports();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();
        Button filterButton = findViewById(R.id.filterButton);

        filterButton.setOnClickListener(v -> {
            fm.beginTransaction().replace(R.id.frameLayout, new filterSports()).commit();
            backgroundFilter.setVisibility(View.VISIBLE);
        });
    }


    /**
     * JSON content is read from local file
     */
    private String loadJSONFromAsset()  {
        String json;
        try {
            InputStream is = getAssets().open("sports.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * JSON content is translated from loadJSONFromAsset
     */
    private void extractSports() throws JSONException {

        JSONArray arr = new JSONArray(loadJSONFromAsset());

        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject sportObject = arr.getJSONObject(i);

                Sport sport = new Sport();
                sport.setName(sportObject.getString("name").toString());
                sport.setDescription(sportObject.getString("description".toString()));
                sport.setLogo(sportObject.getString("logo"));
                sport.setLink(sportObject.getString("link"));


                JSONArray tagList = sportObject.getJSONArray("tags");
                for(int j = 0; j < tagList.length(); j++)
                    sport.addTag(Tag.valueOf(tagList.getString(j)));


                sports.add(sport);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        SportsAdapter sportsAdapter = new SportsAdapter(getApplicationContext(), sports);
        recyclerView.setAdapter(sportsAdapter);


    }
    public void goBack(View view){
        this.onBackPressed();
    }

    /**
     * This method returns a new copy of the full sports list to the fragment
     * This was done to remove an alibi problem
     * @return a new list of the sport
     */
    protected List<Sport> getSportsList(){
        return new ArrayList<>(sports);
    }

    protected void setSavedTags(List<Tag> t){
        savedTags = t;

    }

    protected List<Tag> getSavedTags(){
        return savedTags;
    }
}