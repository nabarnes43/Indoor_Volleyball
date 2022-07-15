package com.example.indoor_volleyball.Fragments.EventFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.FragmentEventFinderBinding;
import com.example.indoor_volleyball.databinding.FragmentEventsAttendingBinding;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class EventsAttendingFragment extends EventListFragment {
    private FragmentEventsAttendingBinding binding;

    public EventsAttendingFragment() {
        // Required empty public constructor
    }

    @Override
    protected ParseQuery<Event> getEventQuery() {
        ParseQuery<Event> query = super.getEventQuery();
        query.whereEqualTo("userRelation", ParseUser.getCurrentUser());
        return query;
    }
}