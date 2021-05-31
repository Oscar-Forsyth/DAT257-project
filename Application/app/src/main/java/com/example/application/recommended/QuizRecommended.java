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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.example.application.R;
import com.example.application.SportsLoader;
import com.example.application.Tag;
import com.example.application.sports.Sport;

/**
 * the fragment that contains the recommended sports
 * it refreshes its recyclerview based on the user's quiz answers
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
    // Used in fragments, don't need to change
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<Integer> resultList;

    // Could be used when factory method is
    private String mParam1;
    private String mParam2;

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

    /**
     * called when this fragment is being created, atm default implementation of onCreate, ie does nothing particular for our fragment
     * @param savedInstanceState last state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * assigns xml-file to the view, and sets listener to a button
     * @return a view of fragment_quiz_recommended.xml
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_recommended, container, false);
        buttonToRetakeQuiz= view.findViewById(R.id.retakeQuizButton);
        buttonToRetakeQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Wizard.class);
            startActivity(intent);
        });
        resultList = requireActivity().getIntent().getIntegerArrayListExtra("QUIZ_RESULTS");

        return view;
    }

    //Required for fragment, just leave

    /**
     * gets list of sports
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recommendedList= requireActivity().findViewById(R.id.recommendedList);
        sports=new ArrayList<>();
        extractSports();

    }

    /**
     * adds points to a tag itself based on the user's answers in the quiz
     * @param list the user's answers from the quiz
     */
    private void addQuizPoints(List<Integer>list){

        fillSportsWithPointsHashMapWithDefaultValues();

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

    /**
     * for each tag in tagWithPoints, add points to the sports with that tag
     */
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

    /**
     * sorts the list of sports based on how many paints they got from the quiz and returns top 5
     * @return list with the 5 sports that got the greatest number of points
     */
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

    /**
     * fills the hashmap with default values (0).
     * These values are later modified based on quiz answers
     */
    private void fillSportsWithPointsHashMapWithDefaultValues(){
        sportsWithPointsHashMap = new HashMap<>();
        for (Sport s : sports){
            sportsWithPointsHashMap.put(s, 0);
        }
    }

    /**
     * loads the saved recommended sports and displays them
     */
    private void extractSports(){
        sports = SportsLoader.extractSavedSports("SavedSportsFile", "SavedSportsKey", requireActivity());

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
       displayTop5(top5Sports);
    }

    /**
     * displays the top 5 sports in the recyclerview
     * @param top5Sports the sports to be displayed in the recyclerview
     */
    private void displayTop5(List <Sport> top5Sports){
        adapter = new QuizRecommendedAdapter(requireActivity().getApplicationContext(), top5Sports, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity().getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recommendedList.setLayoutManager(gridLayoutManager);
        recommendedList.setAdapter(adapter);
    }

    /**
     * saves list of recommendations
     * @param list the list to be saved
     */
    private void saveList(List<Sport> list){
        SharedPreferences.Editor editor = this.requireActivity().getSharedPreferences("Save", Context.MODE_PRIVATE).edit();
        String Sports = "";

        for(Sport e : list)
            Sports = Sports + e.getName() + ",";
        editor.putString("savedRecommendations", Sports);
        editor.apply();
    }

    /**
     * converts recommendations in string format to an actual list with Sport-objects
     * @param string recommendations in string format
     * @return list of recommendations
     */
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