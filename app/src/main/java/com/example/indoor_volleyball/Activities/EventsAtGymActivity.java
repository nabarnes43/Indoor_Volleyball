package com.example.indoor_volleyball.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.indoor_volleyball.Activities.Details.GymDetailActivity;
import com.example.indoor_volleyball.Adapters.EventAdapter;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityEventsAtGymBinding;
import com.example.indoor_volleyball.databinding.ActivityMainBinding;
import com.example.indoor_volleyball.databinding.FragmentEventsCreatedBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class EventsAtGymActivity extends AppCompatActivity {
    ActivityEventsAtGymBinding binding;
    private List<Event> eventsCreated;
    private RecyclerView rvEvents;
    private EventAdapter adapterEvents;
    Gym gym;
    private static final String GYM_KEY = "gym";
    public static Intent newIntent(Context context, Gym gym) {
        Intent i = new Intent(context, EventsAtGymActivity.class);
        i.putExtra(GYM_KEY, Parcels.wrap(gym));
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventsAtGymBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        gym = Parcels.unwrap(getIntent().getParcelableExtra(GYM_KEY));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle(gym.getName());
        rvEvents = binding.rvEventsAtGym;
        eventsCreated = new ArrayList<>();
        adapterEvents = new EventAdapter(this, eventsCreated);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(adapterEvents);
        fetchUserGymsAsync(0);
        binding.fabCreateEventAtGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateEvent(gym);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fetchUserGymsAsync(int i) {
        adapterEvents.clear();
        queryEventsAtGym(gym);
    }

    //Get a list of all the events at the given gym sorted by Date Descending.
    private void queryEventsAtGym(Gym gym) {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        eventQuery.whereEqualTo("gym", gym);
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

    private void goToCreateEvent(Gym gym) {
        String gymId = gym.getObjectId();
        Intent i = new Intent(this, CreateEventActivity.class);
        i.putExtra("gymId", Parcels.wrap(gymId));
        startActivity(i);

    }
}





