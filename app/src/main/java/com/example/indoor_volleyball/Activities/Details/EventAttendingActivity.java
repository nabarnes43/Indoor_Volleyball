package com.example.indoor_volleyball.Activities.Details;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.databinding.ActivityAttendingEventBinding;
import com.example.indoor_volleyball.databinding.ActivityEventCreatorDetailBinding;

import org.parceler.Parcels;

public class EventAttendingActivity extends AppCompatActivity {
    ActivityAttendingEventBinding binding;
    Event event;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAttendingEventBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        event = Parcels.unwrap(getIntent().getParcelableExtra("event"));

        binding.tvCreateEventAttend.setText("Creator: " + event.getCreator().getUsername());
        binding.tvStartTimeAttend.setText("Start Time " + event.getStartTime().toString());
        binding.tvEndTimeAttend.setText("End Time: " + event.getEndTime().toString());
        binding.tvMinPlayersAttend.setText("Min Players: "+ event.getMinCount().toString());
        binding.tvMaxPlayersAttend.setText("Max Players: " + event.getMaxCount().toString());
        binding.tvSkillLevelAttend.setText("Skill Level: " + event.getSkillLevel());
        binding.tvAllowPlusOnesAttend.setText("Allow Plus Ones: " + event.getAllowPlusOnes());
        binding.tvAllowSpectatorsAttend.setText("Allow Spectators " + event.getAllowSpectators());
        binding.tvDetailsAttend.setText("Details: " + event.getDetails());
        binding.tvEventCodeAttend.setText("Event Code: " + event.getEventCode());
        binding.tvTeamRotationAttend.setText("Rotation rule: " + event.getTeamRotation());


    }
}
