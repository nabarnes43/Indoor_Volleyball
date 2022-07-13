package com.example.indoor_volleyball.Activities.Details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.indoor_volleyball.Activities.CreateEventActivity;
import com.example.indoor_volleyball.Activities.EventsAtGymActivity;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityCreateGymBinding;
import com.example.indoor_volleyball.databinding.ActivityGymDetailBinding;
import com.parse.ParseException;

import org.parceler.Parcels;

public class GymDetailActivity extends AppCompatActivity {
    ActivityGymDetailBinding binding;
    public static final String TAG = "GymDetailActivity";
    Gym gym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGymDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        gym = Parcels.unwrap(getIntent().getParcelableExtra("gym"));

        binding.tvGymNameDetail.setText(gym.getName());
        binding.rbGymRatingDetail.setRating(gym.getRating().floatValue());
        //TODO resource strings

        Event event = null;
        try {
            event = gym.getNextEvent();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.itmEventItem.tvDate.setText("Start time: " + event.getStartTime() + " End Time: " + event.getEndTime());
        binding.itmEventItem.tvMinMaxCount.setText(" Min: " + event.getMinCount() + " Max: " + event.getMaxCount());
        binding.itmEventItem.tvSkillLevelEvent.setText("Skill Level: " + event.getSkillLevel());

        binding.fabCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateEvent(gym);
            }
        });

        binding.btViewMoreEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEventsAtGym(gym);
            }
        });

    }

    private void goToCreateEvent(Gym gym) {
        String gymId = gym.getObjectId();
        Intent i = new Intent(this, CreateEventActivity.class);
        i.putExtra("gymId", Parcels.wrap(gymId));
        startActivity(i);

    }

    private void goToEventsAtGym(Gym gym) {
        Intent i = new Intent(this, EventsAtGymActivity.class);
        i.putExtra("gym", Parcels.wrap(gym));
        startActivity(i);

    }



}