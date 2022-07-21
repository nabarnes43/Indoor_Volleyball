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
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class EventsCreatedFragment extends EventListFragment {

    public EventsCreatedFragment() {
        // Required empty public constructor
    }


    @Override
    protected ParseQuery<Event> getEventQuery() {
        ParseQuery<Event> query = super.getEventQuery();
        query.whereEqualTo("creator", ParseUser.getCurrentUser());
        return query;
    }
}