package com.example.indoor_volleyball.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indoor_volleyball.Adapters.GymAdapter;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.QueryActivity;
import com.example.indoor_volleyball.R;


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
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GymFinderFragment extends Fragment {
    public static final String TAG = "PostFragment";
    private SwipeRefreshLayout swipeContainer;
    private GymAdapter adapter;
    private RecyclerView rvGyms;
    List<Gym> allGymsByDistance;

    public GymFinderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gym_finder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvGyms = view.findViewById(R.id.rvGyms);
        //Steps to use the recycler view
        //0. Create layout for one row in the list
        //1. Create the adapter
        allGymsByDistance = new ArrayList<>();
        adapter = new GymAdapter(getContext(), allGymsByDistance);
        //2. Create the data source
        //3. Set the adapter on the recycler view
        rvGyms.setAdapter(adapter);
        //4. set the layout manager on the recycler view
        rvGyms.setLayoutManager(new LinearLayoutManager(getContext()));
        allGymsByDistance(ParseUser.getCurrentUser().getParseGeoPoint("longLat"));


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

    public void fetchTimelineAsync(int i) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        adapter.clear();
        allGymsByDistance(ParseUser.getCurrentUser().getParseGeoPoint("longLat"));


    }

    //Gets a list of all the gyms in order of distance from the user.
    private void allGymsByDistance(ParseGeoPoint userLocation) {
        ParseQuery<Gym> query = new ParseQuery<>("Gym");
        query.whereNear("location", userLocation);
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymList, ParseException e) {
                if (e == null) {
                    allGymsByDistance.addAll(gymList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }

                adapter.notifyDataSetChanged();
            }
        });
    }




    //TODO DELETE WHEN DONE


//    private void queryGyms() {
//        // specify what type of data we want to query - Post.class
//        ParseQuery<Gym> query = ParseQuery.getQuery(Gym.class);
//        // include data referred by user key
//        query.include(Post.KEY_USER);
//        // limit query to latest 20 items
//        query.setLimit(20);
//        // order posts by creation date (newest first)
//        query.addDescendingOrder("createdAt");
//        // start an asynchronous call for posts
//        query.findInBackground(new FindCallback<Post>() {
//            @Override
//            public void done(List<Post> posts, ParseException e) {
//                // check for errors
//                if (e != null) {
//                    Log.e(TAG, "Issue with getting posts", e);
//                    return;
//                }
//                // for debugging purposes let's print every post description to logcat
//                for (Post post : posts) {
//                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
//                }
//
//                // save received posts to list and notify adapter of new data
//                allPosts.addAll(posts);
//                adapter.notifyDataSetChanged();
//                swipeContainer.setRefreshing(false);
//            }
//        });
//    }
}