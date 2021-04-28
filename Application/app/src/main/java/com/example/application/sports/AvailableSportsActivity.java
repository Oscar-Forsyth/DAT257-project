package com.example.application.sports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.FrameLayout;


import android.view.View;
import android.widget.Button;


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
 * This class more or less extratcs the information that's stored in sports.json and puts it into a
 * Arraylist so that it can be used later. It also serves as the base for a new activity
 */
public class AvailableSportsActivity extends AppCompatActivity {
    /**
     * the framework of the list displaying the sports
     */
    RecyclerView recyclerView;
    FrameLayout frameLayout, backgroundFilter;
    Button filterButton;
    List<Sport> sports;
    SportsAdapter sportsAdapter;


    /**
     * the URL for our JSON-file
     * For every update to the JSON-file, a new URL has to be generated so there is probably a better solution
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_sports);
        this.setTitle("Available Sports");


        frameLayout = findViewById(R.id.frameLayout);
        filterButton = findViewById(R.id.filterButton);
        recyclerView = findViewById(R.id.sportsList);
        backgroundFilter = findViewById(R.id.backgroundFilter);
        backgroundFilter.setAlpha((float)(0.6)); //TODO Move to xml


        //Fragments
        FragmentManager fm = getSupportFragmentManager();


        sports = new ArrayList<>();

        try {
            extractSports();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Start filterFragment
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.frameLayout, new filterSports()).commit();
                backgroundFilter.setVisibility(View.VISIBLE);
            }
        });
    }

    protected void backgroundFilterInvis(){
        backgroundFilter.setVisibility(View.INVISIBLE);
    }



    /**
     * JSON content is read from local file
     */

    private String loadJSONFromAsset()  {
        String json = null;
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
        sportsAdapter = new SportsAdapter(getApplicationContext(),sports);
        recyclerView.setAdapter(sportsAdapter);


    }


}