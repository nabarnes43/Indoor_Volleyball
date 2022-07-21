package com.example.indoor_volleyball.Activities.Details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityAttendingEventBinding;
import com.example.indoor_volleyball.databinding.ActivityEventCreatorDetailBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Objects;

public class EventAttendingActivity extends AppCompatActivity {
    private static final String TAG = "EventAttendingActivity";
    ActivityAttendingEventBinding binding;
    private static final String EVENT_ID_KEY = "eventId";
    public static Intent newIntent(Context context, String eventId) {
        Intent i = new Intent(context, EventAttendingActivity.class);
        i.putExtra(EVENT_ID_KEY, Parcels.wrap(eventId));
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendingEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String eventId = Parcels.unwrap(getIntent().getParcelableExtra(EVENT_ID_KEY));
        try {
            Event event = queryEvent(eventId);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            getSupportActionBar().setTitle(event.getGym().getName());
            //TODO can get here but may not be logged in.
            ParseUser creator = event.getCreator();
            String creatorName = creator.getUsername();
            String creatorText = getString(R.string.event_get_creator_text) + creatorName;
            String startTimeText = getString(R.string.event_get_start_time_text) + " " + event.getStartTime().toString();
            String endTimeText = getString(R.string.event_get_end_time_text) + " " + event.getEndTime().toString();
            String minPlayersText = getString(R.string.event_min_players_text) + " "  + event.getMinCount().toString();
            String maxPlayersText = getString(R.string.event_get_max_players_text) + " "   + event.getMaxCount().toString();
            String skillLevelText = getString(R.string.event_get_skill_level_text) + " "   + event.getSkillLevel();
            String allowPlusOnesText = getString(R.string.event_get_allow_plus_ones_text) + " "  + event.getAllowPlusOnes();
            String allowSpectatorsText = getString(R.string.event_get_allow_spectators_text) + " "   + event.getAllowSpectators();
            String detailsText = getString(R.string.event_get_details_text) + " "   + event.getDetails();
            String eventCodeText = getString(R.string.event_get_event_code_text) + " "   + event.getEventCode();
            String rotationText = getString(R.string.event_get_rotation_text) + " "   + event.getTeamRotation();
            ParseFile image = creator.getParseFile("profilePhoto");
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
            if (image != null) {
                Glide.with(this).load(image.getUrl()).circleCrop().into(binding.ivCreatorProfileDetailAttend);
            }
            if (event.getGym().getImage() != null) {
                Glide.with(this).load(event.getGym().getImage().getUrl()).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.ivGymPhotoEventAttending);
            }
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
    }

    private Event queryEvent(String eventId) throws ParseException {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(getString(R.string.event_query_class_name));
        eventQuery.include("creator");
        return eventQuery.get(eventId);
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
