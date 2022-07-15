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

        //TODO go to my location button to go to users location

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
        binding.tvCreateEventDetail.setText(R.string.event_creator_manage_event_text);
        binding.tvStartTimeDetail.setHint(startTimeText);
        binding.tvEndTimeDetail.setHint(endTimeText);
        binding.etMinPlayersDetail.setHint(minPlayersText);
        binding.etMaxPlayersDetail.setHint(maxPlayersText);
        binding.tvSkillLevelDetail.setText(skillLevelText);
        binding.tvAllowPlusOnesDetail.setText(allowPlusOnesText);
        binding.tvAllowSpectatorsDetail.setText(allowSpectatorsText);
        binding.etDetailsDetail.setHint(detailsText);
        binding.etEventCodeDetail.setHint(eventCodeText);
        binding.etTeamRotationDetail.setHint(rotationText);
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
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(getString(R.string.event_query_class_name));
        event = eventQuery.get(eventId);
    }
}
