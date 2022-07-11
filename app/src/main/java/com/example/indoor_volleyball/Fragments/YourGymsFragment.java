package com.example.indoor_volleyball.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indoor_volleyball.Adapters.GymAdapter;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.FragmentGymFinderBinding;
import com.example.indoor_volleyball.databinding.FragmentYourGymsBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class YourGymsFragment extends Fragment {
    FragmentYourGymsBinding binding;
    List<Gym> gymsFollowed;
    GymAdapter adapterUserGyms;
    RecyclerView rvGyms;


    public YourGymsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentYourGymsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_your_gyms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvGyms = binding.rvGyms;
        gymsFollowed = new ArrayList<>();
        adapterUserGyms = new GymAdapter(getContext(), gymsFollowed);
        rvGyms.setAdapter(adapterUserGyms);
        rvGyms.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchUserGymsAsync(0);
    }



    public void fetchUserGymsAsync(int i) {
        adapterUserGyms.clear();
        queryUsersGymsByDistance(ParseUser.getCurrentUser());
    }

        //Get a list of gyms that the user follows.
    private void queryUsersGymsByDistance(ParseUser user) {
        ParseGeoPoint userLocation = user.getParseGeoPoint("longLat");
        ParseQuery<Gym> query = new ParseQuery<>("Gym");
        query.whereEqualTo("usersFollowing", user);
        query.whereNear("location", userLocation);
        query.include("details");
        query.include("startTime");
        query.include("endTime");
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymList, ParseException e) {
                if (e == null) {
                    gymsFollowed.addAll(gymList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
                adapterUserGyms.notifyDataSetChanged();
            }
        });
    }



}
