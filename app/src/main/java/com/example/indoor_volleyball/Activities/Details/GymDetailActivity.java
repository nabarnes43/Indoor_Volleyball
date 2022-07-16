package com.example.indoor_volleyball.Activities.Details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
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

import java.util.Objects;

public class GymDetailActivity extends AppCompatActivity {
    ActivityGymDetailBinding binding;
    private static final String GYM_KEY = "gym";
    public static Intent newIntent(Context context, Gym gym) {
        Intent i = new Intent(context, GymDetailActivity.class);
        i.putExtra(GYM_KEY, Parcels.wrap(gym));
        return i;
    }
    public static final String TAG = "GymDetailActivity";
    Gym gym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGymDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        gym = Parcels.unwrap(getIntent().getParcelableExtra(GYM_KEY));
        binding.rbGymRatingDetail.setRating(gym.getRating().floatValue());
        //TODO resource strings
        Event event = null;
        try {
            event = gym.getNextEvent();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String eventDateText = "";
        String eventMinMaxCountText = "";
        String eventSkillLevel = "";
        if (event != null) {
            eventDateText = getString(R.string.event_get_start_time_text) + event.getStartTime() + " " + getString(R.string.event_get_end_time_text) + event.getEndTime();
            eventMinMaxCountText = getString(R.string.event_item_min_text) + event.getMinCount() + getString(R.string.event_item_max_text) + event.getMaxCount();
            eventSkillLevel = getString(R.string.event_get_skill_level_text) + event.getSkillLevel();
        } else {
            binding.btViewMoreEvents.setText(R.string.no_events);
        }
        binding.rbGymRatingDetail.setRating(gym.getRating().floatValue());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle(gym.getName());
        binding.itmEventItem.tvDate.setText(eventDateText);
        binding.itmEventItem.tvMinMaxCount.setText(eventMinMaxCountText);
        binding.itmEventItem.tvSkillLevelEvent.setText(eventSkillLevel);
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

    private void goToCreateEvent(Gym gym) {
        String gymId = gym.getObjectId();
        Intent i = new Intent(this, CreateEventActivity.class);
        i.putExtra(getString(R.string.gym_id_parcel_tag), Parcels.wrap(gymId));
        startActivity(i);
    }

    private void goToEventsAtGym(Gym gym) {
        Intent i = new Intent(this, EventsAtGymActivity.class);
        i.putExtra(getString(R.string.gym_parcel_tag), Parcels.wrap(gym));
        startActivity(i);
    }
}