package com.example.application.sports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    List<Sport> sports;
    SportsAdapter sportsAdapter;

    private Toolbar toolbar;
    private TextView textView;
    /**
     * the URL for our JSON-file
     * For every update to the JSON-file, a new URL has to be generated so there is probably a better solution
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_sports);

        toolbar = findViewById(R.id.customToolbar);
        textView = (TextView) findViewById(R.id.toolbarText);
        textView.setText("Sports and Committiees");

        recyclerView = findViewById(R.id.sportsList);
        sports = new ArrayList<>();

        try {
            extractSports();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    public void goBack(View view){
        this.onBackPressed();
    }

}