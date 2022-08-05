
package com.example.indoor_volleyball.Fragments.EventFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.indoor_volleyball.Adapters.EventAdapter;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.databinding.FragmentEventsCreatedBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public abstract class EventListFragment extends Fragment {
    private FragmentEventsCreatedBinding binding;
    private List<Event> eventsCreated;
    private RecyclerView rvEvents;
    private EventAdapter adapterEvents;

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventsCreatedBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvNoEvents.setVisibility(View.INVISIBLE);
        rvEvents = binding.rvEvents;
        eventsCreated = new ArrayList<>();
        adapterEvents = new EventAdapter(getContext(), eventsCreated);
        //TODO make this a grid layout and redo event item
        rvEvents.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvEvents.setAdapter(adapterEvents);
        fetchUserEventsAsync(0);
        // Setup refresh listener which triggers new data loading
        binding.swipeContainerEvents.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchUserEventsAsync(0);
                binding.swipeContainerEvents.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        binding.swipeContainerEvents.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchUserEventsAsync(int i) {
        adapterEvents.clear();
        queryUserEventsManaging();
    }

    protected ParseQuery<Event> getEventQuery() {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        return eventQuery;
    }

    private void queryUserEventsManaging() {
        Date date = new Date();
        ParseQuery<Event> eventQuery = getEventQuery();
        eventQuery.orderByAscending("startTime");
        eventQuery.whereGreaterThanOrEqualTo("startTime", date);
        eventQuery.include("creator");
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> eventList, ParseException e) {
                if (e == null) {
                    eventsCreated.addAll(eventList);
                    if (eventList.isEmpty()) {
                        binding.tvNoEvents.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
                adapterEvents.notifyDataSetChanged();
            }
        });
    }
}
