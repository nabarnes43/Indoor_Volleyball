package com.example.indoor_volleyball.Activities.Details;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.databinding.ActivityEventCreatorDetailBinding;

import org.parceler.Parcels;

public class EventCreatorDetailActivity extends AppCompatActivity {
    ActivityEventCreatorDetailBinding binding;
    Event event;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        event = Parcels.unwrap(getIntent().getParcelableExtra("event"));

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
}
