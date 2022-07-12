package com.example.indoor_volleyball.Fragments.GymFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indoor_volleyball.Adapters.GymAdapter;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.databinding.FragmentYourGymsBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class AllGymsFragment extends Fragment {

    private FragmentYourGymsBinding binding;
    private SwipeRefreshLayout swipeContainer;
    private GymAdapter adapterAllGyms;
    private RecyclerView rvGyms;
    List<Gym> allGymsByDistance;

    public AllGymsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentYourGymsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGyms = binding.rvGyms;
        allGymsByDistance = new ArrayList<>();
        adapterAllGyms = new GymAdapter(getContext(), allGymsByDistance);
        rvGyms.setAdapter(adapterAllGyms);
        rvGyms.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchAllGymsAsync(0);
    }


    public void fetchAllGymsAsync(int i) {
        adapterAllGyms.clear();
        allGymsByDistance(ParseUser.getCurrentUser().getParseGeoPoint("longLat"));
    }

    //Gets a list of all the gyms in order of distance from the user.
    private void allGymsByDistance(ParseGeoPoint userLocation) {
        ParseQuery<Gym> query = new ParseQuery<>("Gym");
        query.whereNear("location", userLocation);
        query.include("nextEvent");
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymList, ParseException e) {
                if (e == null) {
                    allGymsByDistance.addAll(gymList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
                adapterAllGyms.notifyDataSetChanged();
            }
        });
    }


}