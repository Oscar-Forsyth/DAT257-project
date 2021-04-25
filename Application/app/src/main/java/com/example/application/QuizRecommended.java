package com.example.application;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    HashMap<Sport, Integer> pairList2 = new HashMap<>();

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

    private void converter(List<Integer>list){

/*
        List<Pair<Tag, ArrayList<Sport>>> tagWithMultipleSports = new ArrayList<>();
        for (Tag t : Tag.values()){
            ArrayList<Sport>arrayWithSports=new ArrayList<>();
            //since this method is in the end of extractSports(), sports is filled with every sport
            for (Sport s : sports){
                if(s.getTags().contains(t)){
                    arrayWithSports.add(s);
                }
            }
            tagWithMultipleSports.add(new Pair<>(t, arrayWithSports));
        }

 */

       // List<Pair<Tag, ArrayList<Sport>>> tagsWithPoints = new ArrayList<>();
        fillPairListWithDefaultValues();
        List <Tag> tagsWithPoints = new ArrayList<>();
        System.out.println("converter körs");

        for (int questionNr=0; questionNr<list.size(); questionNr++){
            //answer to question (0,1,2,3) where 0=No answer, 1=Yes, 2=No, 3=Sometimes
            int ans = list.get(questionNr);
            switch (questionNr){

                case 0:
                    System.out.println("hejsan");
                    //add point to the tag itself: new pair(tag, sportsList) och lägg in i tagWithMultipleSports så allt i tagWithMultSports är taggar som fått poäng
                    //för varje tag som fått poäng, dvs de som finns i tagWithMultipleSports, lägg till poäng till alla sporter som har den taggen

                    if (ans==0){
                        //do nothing (user did not check any alternative)
                        System.out.println("meiokrgmiopahetmrhtmiopårphtmsrthmprtsnmpoåsrnht");
                    }
                    if (ans==1){
                        //add GROUP
                        tagsWithPoints.add(Tag.GROUP);
                        System.out.println("auirngehkkjrhshrstrt");
                    }
                    if (ans==2){
                        //add INDIVIDUAL
                        tagsWithPoints.add(Tag.INDIVIDUAL);
                        System.out.println("hepåo,torpohrt,ohrstt");
                    }

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
            }

        }
        System.out.println("tagsWithPoints.size should be 1: " + tagsWithPoints.size());
        for (Tag t : tagsWithPoints){
            for (Sport s : pairList2.keySet()){
                System.out.println(s.getTags().size());
                if(s.getTags().contains(t)){

                    System.out.println("");
                    Integer check = pairList2.get(s);
                    if (check == null) {
                        Log.e("Error!", "Integer connected to Sport is null!");
                        return;
                    }
                    pairList2.put(s, check + 1);
                }
            }
        }

        int b=0;
        for (Sport a : pairList2.keySet()){

            System.out.println("pairList2 på plats " +b+": " + a.getName() + ", poäng: " + pairList2.get(a));

            b++;
        }

        /*
        for (Pair <Sport, Integer> p : pairList){
            for (Tag t : tagsWithPoints){
                if(p.getL().getTags().contains(t)){
                    int value = p.getR();
                    p.setR(value+1);
                }
            }
        }

         */

    }

    private List<Sport> onlyTop5(){
        List<Sport>top5Sports = new ArrayList<>();

        List<Map.Entry<Sport, Integer>> tmpListForSorting = new LinkedList<>(pairList2.entrySet());
        Collections.sort(tmpListForSorting, Collections.reverseOrder(new Comparator<Map.Entry<Sport, Integer>>() {
            @Override
            public int compare(Map.Entry<Sport, Integer> o1, Map.Entry<Sport, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        }));

        int iterationCount=0;
        for (Sport s : pairList2.keySet()){
            if(iterationCount>=5){
                break;
            }
            top5Sports.add(s);
            iterationCount++;
        }
        return top5Sports;
    }

    private void fillPairListWithDefaultValues(){
        pairList2 = new HashMap<>();
        for (Sport s : sports){
            pairList2.put(s, 0);
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
                
                sports.add(sport);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //temporary for tests
        List <Integer> randList = new ArrayList<>();
        //adds INDIVIDUAL to tagsWithPoints
        randList.add(2);
        //adds points to every sport that can be found in tagsWithPoints
        converter(randList);
        List <Sport> top5Sports = onlyTop5();
        System.out.println("onlyTop5.size() bör vara 5: " + onlyTop5().size());

        //converter(list, pairList); list is the argument sent from Wizard


        adapter = new AdapterQuizRecommended(requireActivity().getApplicationContext(), top5Sports);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity().getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recommendedList.setLayoutManager(gridLayoutManager);
        recommendedList.setAdapter(adapter);

/*
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new Adapter(getApplicationContext(),sports);
        recyclerView.setAdapter(adapter);

 */

    }
    /*
    private class Pair<L,R> {
        private L l;
        private R r;
        public Pair(L l, R r){
            this.l = l;
            this.r = r;
        }

        public L getL(){ return l; }
        public R getR(){ return r; }
        public void setL(L l){ this.l = l; }
        public void setR(R r){ this.r = r; }
    }

     */

}