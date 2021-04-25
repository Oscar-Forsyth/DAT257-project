package com.example.application;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizRecommended#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizRecommended extends Fragment {

    Button buttonToMainMenu;
    Button buttonToRetakeQuiz;
    RecyclerView recommendedList;

    List<Sport> sports;
    AdapterQuizRecommended adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public QuizRecommended() {
        // Required empty public constructor
    }

    public static QuizRecommended newInstance(String param1, String param2) {
        QuizRecommended fragment = new QuizRecommended();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //GridLayoutManager grd = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        //adapter = new AdapterQuizRecommended();
        //gridView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_recommended, container, false);
        buttonToMainMenu= view.findViewById(R.id.buttonToMainMenu);
        buttonToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainMenu.class);
                startActivity(intent);
            }
        });
        buttonToRetakeQuiz= view.findViewById(R.id.retakeQuizButton);
        buttonToRetakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Wizard.class);
                startActivity(intent);
            }
        });
        List<Integer> resultList = requireActivity().getIntent().getIntegerArrayListExtra("QUIZ_RESULTS");
        if(resultList!=null){
            System.out.println("--\n");
            for (int i = 0; i < resultList.size(); i++) {
                System.out.println("Question: " + i + "result: " + resultList.get(i).toString());
            }
            System.out.println("--\n");
        }


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recommendedList= requireActivity().findViewById(R.id.recommendedList);
        sports=new ArrayList<>();

        /*
        adapter = new AdapterQuizRecommended();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recommendedList.setLayoutManager(gridLayoutManager);
        recommendedList.setAdapter(adapter);
         */
        try {
            extractSports();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String loadJSONFromAsset() throws JSONException {
        String json = null;
        try {
            InputStream is = requireActivity().getAssets().open("sports.json");
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
                sports.add(sport);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // currently in onCreate: sports=new ArrayList<>();
        //TODO sports is not filled yet, due to findViewByID
        adapter = new AdapterQuizRecommended(requireActivity().getApplicationContext(), sports);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity().getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recommendedList.setLayoutManager(gridLayoutManager);
        recommendedList.setAdapter(adapter);

/*
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new Adapter(getApplicationContext(),sports);
        recyclerView.setAdapter(adapter);

 */

    }

}