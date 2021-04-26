package com.example.application.recommended;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.application.MainMenu;
import com.example.application.R;
import com.example.application.Tag;
import com.example.application.sports.Sport;

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
    HashMap<Sport, Integer> sportsWithPointsHashMap = new HashMap<>();
    List <Tag> tagsWithPoints = new ArrayList<>();

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
                Intent intent = new Intent(getActivity(), MainMenu.class);
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

    private void converter(List<Integer>list){
        fillSportsWithPointsHashMapWithDefaultValues();
        //adds point to the tag itself by putting it into tagWithPoints
        for (int questionNr=0; questionNr<list.size(); questionNr++){
            //answer to question (0,1,2,3) where 0=No answer, 1=Yes, 2=No, 3=Sometimes
            int ans = list.get(questionNr);
            switch (questionNr){
                case 0:
                    if (ans==1){
                        tagsWithPoints.add(Tag.GROUP);
                    }
                    if (ans==2){
                        //add INDIVIDUAL
                        tagsWithPoints.add(Tag.INDIVIDUAL);
                    }
                    break;
                case 1:
                    if (ans==1){
                        tagsWithPoints.add(Tag.OUTDOORS);

                    }
                    if (ans==2){
                        tagsWithPoints.add(Tag.INDOORS);
                    }
                    break;
                case 2:
                    if (ans==1){
                        tagsWithPoints.add(Tag.ENDURANCE);

                    }
                    if (ans==2){
                        tagsWithPoints.add(Tag.HIGHINTENSITY);
                    }
                    break;
                case 3:
                    if (ans==1){
                        tagsWithPoints.add(Tag.TECHNIQUE);
                    }
                    break;
                case 4:
                    if (ans==1){
                        tagsWithPoints.add(Tag.WATERSPORT);
                    }
                    break;
                case 5:
                    if (ans==1){
                        tagsWithPoints.add(Tag.BALLGAME);

                    }
                    if (ans==2){
                        tagsWithPoints.add(Tag.RACKETSPORT);
                    }
                    if (ans==3){
                        tagsWithPoints.add(Tag.EXTREMESPORT);
                    }
                    break;
            }

        }
        addPointsToSportsWithTag();
    }

    //for each tag in tagWithPoints, add points to the sports with that tag
    private void addPointsToSportsWithTag(){
        for (Tag t : tagsWithPoints){
            for (Sport s : sportsWithPointsHashMap.keySet()){
                if(s.getTags().contains(t)){

                    Integer check = sportsWithPointsHashMap.get(s);
                    if (check == null) {
                        Log.e("Error!", "Integer connected to Sport is null!");
                        return;
                    }
                    sportsWithPointsHashMap.put(s, check + 1);
                }
            }
        }
    }

    //returns a list that only contains the 5 sports with the most points
    private List<Sport> onlyTop5(){
        List<Sport>top5Sports = new ArrayList<>();

        List<Map.Entry<Sport, Integer>> tmpListForSorting = new LinkedList<>(sportsWithPointsHashMap.entrySet());
        Collections.sort(tmpListForSorting, Collections.reverseOrder(new Comparator<Map.Entry<Sport, Integer>>() {
            @Override
            public int compare(Map.Entry<Sport, Integer> o1, Map.Entry<Sport, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        }));
        int b=0;

        System.out.println("Sorterat: -----------------------------------");
        for (Map.Entry<Sport, Integer> a : tmpListForSorting){

            System.out.println("tmpList på plats " +b+": " + a.getKey().getName()+ ", poäng: " + a.getValue());

            b++;
        }

        int iterationCount=0;
        for (Map.Entry<Sport, Integer> a : tmpListForSorting){
            if(iterationCount>=5){
                break;
            }
            top5Sports.add(a.getKey());
            iterationCount++;
        }

        return top5Sports;
    }

    private void fillSportsWithPointsHashMapWithDefaultValues(){
        sportsWithPointsHashMap = new HashMap<>();
        for (Sport s : sports){
            sportsWithPointsHashMap.put(s, 0);
        }
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
                JSONArray arrOfTags = sportObject.getJSONArray("tags");
                for (int j = 0; j < arrOfTags.length(); j++) {
                    sport.setTag(arrOfTags.getString(j));
                }
                
                sports.add(sport);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //temporary for tests
        List <Integer> randList = new ArrayList<>();
        //adds INDIVIDUAL to tagsWithPoints
        randList.add(2);
        //adds OUTDOORS
        randList.add(1);
        //adds HIGHINTENSITY
        randList.add(2);
        randList.add(0);
        randList.add(0);
        //adds RACKETSPORT
        randList.add(2);

        //adds points to every sport that can be found in tagsWithPoints
        converter(randList);
        List <Sport> top5Sports = onlyTop5();

        adapter = new AdapterQuizRecommended(requireActivity().getApplicationContext(), top5Sports);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity().getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recommendedList.setLayoutManager(gridLayoutManager);
        recommendedList.setAdapter(adapter);
    }

}