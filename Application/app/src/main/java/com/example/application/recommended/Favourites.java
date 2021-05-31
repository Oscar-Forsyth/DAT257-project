package com.example.application.recommended;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.application.R;
import com.example.application.SportsLoader;
import com.example.application.sports.Sport;

import java.util.ArrayList;
import java.util.List;


/**
 * Responsible for the fragment that is opened when the user clicks on "Favourites"-tab in "Your Sports"
 * A simple {@link Fragment} subclass.
 * Use the {@link Favourites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favourites extends Fragment {

    // Used in fragments, don't need to change
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Could be used when factory method is
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
     * assigns some data to the view
     * @return a view of fragment_favourites.xml
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        emptyListTextView = view.findViewById(R.id.emptyListTextView);
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * extracts the saved list of favourites and displays them in the fragment's recyclerview
     * @param view the view that was created
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        favouritesRecyclerView = requireActivity().findViewById(R.id.favouritesList);
        extractSavedFavourites();
    }

    /**
     * loads data from saved list of favourite sports and displays them
     */
    private void extractSavedFavourites(){
        favouriteSportsList = SportsLoader.extractSavedSports("SavedFavouritesFile", "SavedFavouritesKey", requireActivity());

        if (favouriteSportsList.isEmpty()){
            emptyListTextView.setVisibility(View.VISIBLE);
            favouritesRecyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            emptyListTextView.setVisibility(View.INVISIBLE);
            favouritesRecyclerView.setVisibility(View.VISIBLE);
        }
        refreshRecyclerView();
    }

    /**
     * displays updated version of the list of favourites
     */
    private void refreshRecyclerView(){
        adapter = new QuizRecommendedAdapter(requireActivity().getApplicationContext(), favouriteSportsList, true);
        favouritesRecyclerView.setItemViewCacheSize(favouriteSportsList.size());
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        favouritesRecyclerView.setAdapter(adapter);
    }
}