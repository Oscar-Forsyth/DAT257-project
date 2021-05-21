package com.example.application.recommended;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.Scanner;

import com.example.application.R;
import com.example.application.Tag;
import com.example.application.sports.Sport;

//TODO JavaDoc doesn't really describe what the class does
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizRecommended#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizRecommended extends Fragment {

    Button buttonToRetakeQuiz;
    RecyclerView recommendedList;

    List<Sport> sports;
    QuizRecommendedAdapter adapter;
    HashMap<Sport, Integer> sportsWithPointsHashMap = new HashMap<>();
    List <Tag> tagsWithPoints = new ArrayList<>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<Integer> resultList;

    private String mParam1;
    private String mParam2;

    private Toolbar toolbar;
    private TextView textView;

    public QuizRecommended() {
        // Required empty public constructor
    }

    //Required for fragment, just leave
    public static QuizRecommended newInstance(String param1, String param2) {
        QuizRecommended fragment = new QuizRecommended();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //Required for fragment, just leave
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //Required for fragment, just leave
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_recommended, container, false);
        buttonToRetakeQuiz= view.findViewById(R.id.retakeQuizButton);
        buttonToRetakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Wizard.class);
                startActivity(intent);
            }
        });
        resultList = requireActivity().getIntent().getIntegerArrayListExtra("QUIZ_RESULTS");

        //TODO @Laban / @Goat, ta bort era SOUTS >:(
        if(resultList!=null){
            System.out.println("--\n");
            for (int i = 0; i < resultList.size(); i++) {
                System.out.println("Question: " + i + "result: " + resultList.get(i).toString());
            }
            System.out.println("--\n");
        }
        return view;
    }

    //Required for fragment, just leave
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

    //TODO Small cleanup
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

    private void addQuizPoints(List<Integer>list){
        fillSportsWithPointsHashMapWithDefaultValues();
        //adds point to the tag itself by putting it into tagWithPoints

        if(list!=null){
            for (int questionNr=0; questionNr<list.size(); questionNr++){
                //answer to question (0,1,2,3) where 0=No answer, 1=Yes, 2=No, 3=Sometimes
                int ans = list.get(questionNr);
                switch (questionNr){

                    case 0:
                        if (ans==1){
                            tagsWithPoints.add(Tag.INDOOR);
                        }
                        if (ans==2){
                            //add INDIVIDUAL
                            tagsWithPoints.add(Tag.OUTDOOR);
                        }
                        break;
                    case 1:
                        if (ans==1){
                            tagsWithPoints.add(Tag.GROUP);

                        }
                        if (ans==2){
                            tagsWithPoints.add(Tag.INDIVIDUAL);
                        }
                        if (ans==3){
                            tagsWithPoints.add(Tag.GROUP_AND_INDIVIDUAL);
                        }
                        break;
                    case 2:
                        if (ans==1){
                            tagsWithPoints.add(Tag.INTENSE);

                        }
                        if (ans==2){
                            tagsWithPoints.add(Tag.ENDURANCE);
                        }
                        break;
                    case 3:
                        if (ans==1){
                            tagsWithPoints.add(Tag.BALLGAME);
                        }
                        if(ans ==2){
                            tagsWithPoints.add(Tag.RACKET_SPORT);
                        }
                        if(ans ==3){
                            tagsWithPoints.add(Tag.PRECISION);
                        }
                        if(ans ==4){
                            tagsWithPoints.add(Tag.NATURE);
                        }
                        if(ans ==5){
                            tagsWithPoints.add(Tag.COMPLEX_MOVEMENTS);
                        }
                        break;
                }

            }
            addPointsToSportsWithTag();
        }
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
    //TODO Shorten name plox holy shit
    private void fillSportsWithPointsHashMapWithDefaultValues(){
        sportsWithPointsHashMap = new HashMap<>();
        for (Sport s : sports){
            sportsWithPointsHashMap.put(s, 0);
        }
    }

    //TODO This method is really big, breaking it down to smaller private methods with clear names could help
    // There is also some temporary code for tests
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

        //adds points to every sport that can be found in tagsWithPoints
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Save", Context.MODE_PRIVATE);
        String savedSports = prefs.getString("savedRecommendations", null);
        List <Sport> top5Sports;

        if(savedSports == null) {
            addQuizPoints(resultList);
            top5Sports = onlyTop5();
            saveList(top5Sports);
        } else
            top5Sports = retrieveList(savedSports);


        adapter = new QuizRecommendedAdapter(requireActivity().getApplicationContext(), top5Sports, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity().getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recommendedList.setLayoutManager(gridLayoutManager);
        recommendedList.setAdapter(adapter);
    }

    private void saveList(List<Sport> list){
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("Save", Context.MODE_PRIVATE).edit();
        String Sports = "";

        for(Sport e : list)
            Sports = Sports + e.getName() + ",";
        System.out.println(Sports);
        editor.putString("savedRecommendations", Sports);
        editor.apply();
    }

    private List<Sport> retrieveList(String string){
        List <Sport> list = new ArrayList<>();
        Scanner sc = new Scanner(string).useDelimiter(",");
        String sport;
        while(sc.hasNext()) {
            sport = sc.next();
            for(Sport e : sports)
                if(e.getName().equals(sport))
                    list.add(e);
        }
        sc.close();
        return list;
    }

}