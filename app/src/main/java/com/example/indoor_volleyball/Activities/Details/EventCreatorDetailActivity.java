package com.example.indoor_volleyball.Activities.Details;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityCreateGymBinding;
import com.example.indoor_volleyball.databinding.ActivityEventCreatorDetailBinding;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.Objects;

public class EventCreatorDetailActivity extends AppCompatActivity {
    private static final String TAG = "EventCreatorDetail";
    ActivityEventCreatorDetailBinding binding;
    Event event;
    String eventId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventCreatorDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//go to my location button to go to users location
// Have map refresh when zooming in and out.
//Display gyms your a member of in a different color.
        eventId = Parcels.unwrap(getIntent().getParcelableExtra("eventId"));
        try {
            queryEvent(eventId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle(event.getGym().getName());
        binding.tvCreateEventDetail.setText("Manage Event");
        binding.tvStartTimeDetail.setHint("Start Time " + event.getStartTime().toString());
        binding.tvEndTimeDetail.setHint("End Time: " + event.getEndTime().toString());
        binding.etMinPlayersDetail.setHint("Min Players: "+ event.getMinCount().toString());
        binding.etMaxPlayersDetail.setHint("Max Players: " + event.getMaxCount().toString());
        binding.tvSkillLevelDetail.setText("Skill Level: " + event.getSkillLevel());
        binding.tvAllowPlusOnesDetail.setText("Allow Plus Ones: " + event.getAllowPlusOnes());
        binding.tvAllowSpectatorsDetail.setText("Allow Spectators " + event.getAllowSpectators());
        binding.etDetailsDetail.setHint("Details: " + event.getDetails());
        binding.etEventCodeDetail.setHint("Event Code: " + event.getEventCode());
        binding.etTeamRotationDetail.setHint("Rotation rule: " + event.getTeamRotation());
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

    private void queryEvent(String eventId) throws ParseException {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery("Event");
        event = eventQuery.get(eventId);
    }

}
