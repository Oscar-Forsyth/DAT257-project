package com.example.application.recommended;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.example.application.R;
import com.example.application.SportsLoader;
import com.example.application.sports.Sport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favourites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favourites extends Fragment {

    // Used in fragments, don't need to change
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    List<Sport> favouriteSportsList = new ArrayList<>();
    RecyclerView favouritesRecyclerView;
    QuizRecommendedAdapter adapter;

    private TextView emptyListTextView;


    /**
     * Required empty constructor, currently not in use
     */
    public Favourites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favourites.
     */
    // Required in fragments
    public static Favourites newInstance(String param1, String param2) {
        Favourites fragment = new Favourites();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        //TODO  om favourites är tom ska emptyListTextView.setVisibility(View.INVISIBLE)
        emptyListTextView = view.findViewById(R.id.emptyListTextView);
        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        favouritesRecyclerView = requireActivity().findViewById(R.id.favouritesList);
        extractSavedFavourites();
    }

    private void extractSavedFavourites(){
        favouriteSportsList = SportsLoader.extractSavedSports("SavedFavouritesFile", "SavedFavouritesKey", requireActivity());

        if (favouriteSportsList.isEmpty()){
            emptyListTextView.setVisibility(View.VISIBLE);
            favouritesRecyclerView.setVisibility(View.INVISIBLE);
            System.out.println("list is empty");
        }
        else {
            emptyListTextView.setVisibility(View.INVISIBLE);
            favouritesRecyclerView.setVisibility(View.VISIBLE);
            System.out.println("list is not empty");
        }
        refreshRecyclerView();
    }
    private void refreshRecyclerView(){
        adapter = new QuizRecommendedAdapter(requireActivity().getApplicationContext(), favouriteSportsList, true);
        favouritesRecyclerView.setItemViewCacheSize(favouriteSportsList.size());
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        favouritesRecyclerView.setAdapter(adapter);
    }

}