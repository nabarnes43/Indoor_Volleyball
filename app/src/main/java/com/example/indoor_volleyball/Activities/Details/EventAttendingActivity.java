package com.example.indoor_volleyball.Activities.Details;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityAttendingEventBinding;
import com.example.indoor_volleyball.databinding.ActivityEventCreatorDetailBinding;

import org.parceler.Parcels;

import java.util.Objects;

public class EventAttendingActivity extends AppCompatActivity {
    ActivityAttendingEventBinding binding;
    Event event;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendingEventBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        event = Parcels.unwrap(getIntent().getParcelableExtra(getString(R.string.event_parcel_tag)));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle(event.getGym().getName());
        String creatorText = getString(R.string.event_get_creator_text) + event.getCreator().getUsername();
        String startTimeText = getString(R.string.event_get_start_time_text) + event.getStartTime().toString();
        String endTimeText = getString(R.string.event_get_end_time_text) + event.getEndTime().toString();
        String minPlayersText = getString(R.string.event_min_players_text) + event.getMinCount().toString();
        String maxPlayersText = getString(R.string.event_get_max_players_text) + event.getMaxCount().toString();
        String skillLevelText = getString(R.string.event_get_skill_level_text) + event.getSkillLevel();
        String allowPlusOnesText = getString(R.string.event_get_allow_plus_ones_text) + event.getAllowPlusOnes();
        String allowSpectatorsText = getString(R.string.event_get_allow_spectators_text) + event.getAllowSpectators();
        String detailsText = getString(R.string.event_get_details_text) + event.getDetails();
        String eventCodeText = getString(R.string.event_get_event_code_text) + event.getEventCode();
        String rotationText = getString(R.string.event_get_rotation_text) + event.getTeamRotation();
        binding.tvCreateEventAttend.setText(creatorText);
        binding.tvStartTimeAttend.setText(startTimeText);
        binding.tvEndTimeAttend.setText(endTimeText);
        binding.tvMinPlayersAttend.setText(minPlayersText);
        binding.tvMaxPlayersAttend.setText(maxPlayersText);
        binding.tvSkillLevelAttend.setText(skillLevelText);
        binding.tvAllowPlusOnesAttend.setText(allowPlusOnesText);
        binding.tvAllowSpectatorsAttend.setText(allowSpectatorsText);
        binding.tvDetailsAttend.setText(detailsText);
        binding.tvEventCodeAttend.setText(eventCodeText);
        binding.tvTeamRotationAttend.setText(rotationText);
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
}
