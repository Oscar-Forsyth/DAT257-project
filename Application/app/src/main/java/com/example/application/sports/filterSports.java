package com.example.application.sports;

import android.graphics.Color;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.application.R;
import com.example.application.Tag;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link filterSports#newInstance} factory method to
 * create an instance of this fragment.
 */
public class filterSports extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageButton closeButton;
    private final Fragment frag = this;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public filterSports() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment filterSports.
     */
    // TODO: Rename and change types and number of parameters
    public static filterSports newInstance(String param1, String param2) {
        filterSports fragment = new filterSports();
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

        View rootView = inflater.inflate(R.layout.fragment_filter_sports, container, false);
        List<Tag> tags = new ArrayList<Tag>(EnumSet.allOf(Tag.class));
        FlexboxLayout flexboxLayout = rootView.findViewById(R.id.flexLayout);
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);

        for (int i = 0; i < tags.size(); i++) {
            Button b = new Button(getActivity());
            b.setText(tags.get(i).toString());
            b.setTextSize(20);
            b.setBackground(this.getResources().getDrawable(R.drawable.buttonbackground));
            b.setPadding(25, 5, 25, 5);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(b.isSelected()){
                        b.setBackground(getResources().getDrawable(R.drawable.buttonbackground));
                        b.setTextColor(Color.BLACK);
                        b.setSelected(false);
                    }
                    else {
                        b.setBackground(getResources().getDrawable(R.drawable.buttonbackgroundchecked));
                        b.setTextColor(Color.WHITE);
                        b.setSelected(true);
                    }
                }
            });
            flexboxLayout.addView(b, lp);
        }


        //Functionality for closeButton that closes the filter interface
        closeButton = rootView.findViewById(R.id.closeImage);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 
                //requireActivity().getFragmentManager().popBackStack();
                //requireActivity().onBackPressed();
                //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                //row 123 and 124 accomplish the same thing
                //getFragmentManager().beginTransaction().remove(filterSports.this).commit();
                requireActivity().getSupportFragmentManager().beginTransaction().remove(filterSports.this).commit();
                requireActivity().findViewById(R.id.backgroundFilter).setVisibility(View.INVISIBLE);



                System.out.println("tryckte på close");

            }
        });




        // Inflate the layout for this fragment
        return rootView;
    }







}