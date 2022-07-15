
package com.example.indoor_volleyball.Fragments.EventFragments;

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

import com.example.indoor_volleyball.Adapters.EventAdapter;
import com.example.indoor_volleyball.Adapters.GymAdapter;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.FragmentEventFinderBinding;
import com.example.indoor_volleyball.databinding.FragmentEventsCreatedBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
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
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvEvents = binding.rvEvents;
        eventsCreated = new ArrayList<>();
        adapterEvents = new EventAdapter(getContext(), eventsCreated);
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEvents.setAdapter(adapterEvents);
        fetchUserGymsAsync(0);
    }


    public void fetchUserGymsAsync(int i) {
        adapterEvents.clear();
        queryUserEventsManaging();
    }

    protected ParseQuery<Event> getEventQuery() {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        return eventQuery;
    }

//TODO orgaize by date

    private void queryUserEventsManaging() {
        ParseQuery<Event> eventQuery = getEventQuery();
        eventQuery.orderByAscending("startTime");
        eventQuery.include("creator");
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> eventList, ParseException e) {
                if (e == null) {
                    eventsCreated.addAll(eventList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
                adapterEvents.notifyDataSetChanged();
            }
        });
    }
}
