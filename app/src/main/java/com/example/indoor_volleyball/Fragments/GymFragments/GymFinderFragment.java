package com.example.indoor_volleyball.Fragments.GymFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indoor_volleyball.Adapters.GymAdapter;
import com.example.indoor_volleyball.Adapters.GymsPagerAdapter;
import com.example.indoor_volleyball.Models.Gym;


import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.indoor_volleyball.databinding.FragmentGymFinderBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;


public class GymFinderFragment extends Fragment {
    public static final String TAG = "GYMFINDERBINDER";
    private FragmentGymFinderBinding binding;
    private SwipeRefreshLayout swipeContainer;
    private GymAdapter adapterAllGyms;
    private RecyclerView rvGyms;
    List<Gym> gymsFollowed;
    List<Gym> allGymsByDistance;

    public GymFinderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGymFinderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    //Sub-classes have get data method that's it.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GymsPagerAdapter gymsPagerAdapter = new GymsPagerAdapter(this);
        binding.vpGymList.setAdapter(gymsPagerAdapter);
        TabLayout tlGymsList = binding.tlGymsList;
        new TabLayoutMediator(tlGymsList, binding.vpGymList,
                (tab, position) -> tab.setText(gymsPagerAdapter.getTitle(position))).attach();


//        rvGyms = binding.rvGyms;
//        allGymsByDistance = new ArrayList<>();
//        gymsFollowed = new ArrayList<>();
//        adapterAllGyms = new GymAdapter(getContext(), allGymsByDistance);
//        adapterUserGyms = new UserGymAdapter(getContext(), gymsFollowed);
//        rvGyms.setAdapter(adapterAllGyms);
//        rvGyms.setLayoutManager(new LinearLayoutManager(getContext()));
//        allGymsByDistance(ParseUser.getCurrentUser().getParseGeoPoint("longLat"));


        //TODO refresh listener.
//        //swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);


    }

//    public void fetchAllGymsAsync(int i) {
//        adapterAllGyms.clear();
//        allGymsByDistance(ParseUser.getCurrentUser().getParseGeoPoint("longLat"));
//    }
//
//    public void fetchUserGymsAsync(int i) {
//        adapterUserGyms.clear();
//        queryUsersGymsByDistance(ParseUser.getCurrentUser());
//    }

//    //Gets a list of all the gyms in order of distance from the user.
//    private void allGymsByDistance(ParseGeoPoint userLocation) {
//        ParseQuery<Gym> query = new ParseQuery<>("Gym");
//        query.whereNear("location", userLocation);
//        query.include("nextEvent");
//        query.findInBackground(new FindCallback<Gym>() {
//            @Override
//            public void done(List<Gym> gymList, ParseException e) {
//                if (e == null) {
//                    allGymsByDistance.addAll(gymList);
//                } else {
//                    Log.d("item", "Error: " + e.getMessage());
//                }
//                adapterAllGyms.notifyDataSetChanged();
//            }
//        });
//
//    }
//
//    //Get a list of gyms that the user follows.
//    private void queryUsersGymsByDistance(ParseUser user) {
//        ParseGeoPoint userLocation = user.getParseGeoPoint("longLat");
//        ParseQuery<Gym> query = new ParseQuery<>("Gym");
//        query.whereEqualTo("usersFollowing", user);
//        query.whereNear("location", userLocation);
//        query.include("details");
//        query.include("startTime");
//        query.include("endTime");
//        query.findInBackground(new FindCallback<Gym>() {
//            @Override
//            public void done(List<Gym> gymList, ParseException e) {
//                if (e == null) {
//                    gymsFollowed.addAll(gymList);
//                } else {
//                    Log.d("item", "Error: " + e.getMessage());
//                }
//                adapterUserGyms.notifyDataSetChanged();
//            }
//        });
//    }
}