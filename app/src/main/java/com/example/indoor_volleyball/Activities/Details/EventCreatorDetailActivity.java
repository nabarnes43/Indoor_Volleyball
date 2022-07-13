package com.example.indoor_volleyball.Activities.Details;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.databinding.ActivityCreateGymBinding;
import com.example.indoor_volleyball.databinding.ActivityEventCreatorDetailBinding;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

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


        eventId = Parcels.unwrap(getIntent().getParcelableExtra("eventId"));
        try {
            queryEvent(eventId);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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



    private void queryEvent(String eventId) throws ParseException {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery("Event");
        event = eventQuery.get(eventId);
    }

}
