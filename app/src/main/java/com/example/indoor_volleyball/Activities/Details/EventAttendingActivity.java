package com.example.indoor_volleyball.Activities.Details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.indoor_volleyball.Activities.CreateEventActivity;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityAttendingEventBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventAttendingActivity extends AppCompatActivity {
    private static final String TAG = "EventAttendingActivity";
    ActivityAttendingEventBinding binding;
    private List<String> eventIds;
    private static final String EVENT_ID_KEY = "eventId";
    public static Intent newIntent(Context context, String eventId) {
        Intent i = new Intent(context, EventAttendingActivity.class);
        i.putExtra(EVENT_ID_KEY, Parcels.wrap(eventId));
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventIds = new ArrayList<>();
        binding = ActivityAttendingEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String eventId = Parcels.unwrap(getIntent().getParcelableExtra(EVENT_ID_KEY));
        try {
            Event event = queryEvent(eventId);
            queryUserEventsAttending();
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            getSupportActionBar().setTitle(event.getGym().getName().toLowerCase());
            //TODO can get here but may not be logged in.
            ParseUser creator = event.getCreator();
            String creatorName = creator.getUsername();
            String creatorText = getString(R.string.event_get_creator_text) + creatorName;
            String startTimeText = getString(R.string.event_get_start_time_text) + " " + CreateEventActivity.dateFormat.format(event.getStartTime());
            String endTimeText = getString(R.string.event_get_end_time_text) + " " + CreateEventActivity.dateFormat.format(event.getEndTime());
            String minPlayersText = getString(R.string.event_min_players_text) + " "  + event.getMinCount().toString() + "   ";
            String maxPlayersText = getString(R.string.event_get_max_players_text) + " "   + event.getMaxCount().toString();
            String skillLevelText = getString(R.string.event_get_skill_level_text) + " "   + event.getSkillLevel() ;
            String allowPlusOnesText = getString(R.string.event_get_allow_plus_ones_text) + " "  + event.getAllowPlusOnes();
            String allowSpectatorsText = getString(R.string.event_get_allow_spectators_text) + " "   + event.getAllowSpectators();
            String detailsText = getString(R.string.event_get_details_text) + " "   + event.getDetails();
            String eventCodeText = getString(R.string.event_get_event_code_text) + " "   + event.getEventCode() + "  ";
            String rotationText = getString(R.string.event_get_rotation_text) + " "   + event.getTeamRotation();
            ParseFile image = event.getGym().getImage();
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
            if (event.getGym().getImage() != null) {
                Glide.with(this).load(event.getGym().getImage().getUrl()).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.ivGymPhotoEventAttending);
            } else {
                Glide.with(this).load(R.drawable.icon_gym_black).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.ivGymPhotoEventAttending);
            }
            ParseUser user = ParseUser.getCurrentUser();
            final boolean[] containsEvent = new boolean[1];

            if (eventIds.contains(eventId)) {
                containsEvent[0] = true;
                binding.btFollowEvent.setText(R.string.unfollow_event);
                binding.btFollowEvent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_do_not_disturb_24, 0, 0, 0);
            } else {
                containsEvent[0] = false;
                binding.btFollowEvent.setText(R.string.follow_event);
                binding.btFollowEvent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_24, 0, 0, 0);
            }
            binding.btFollowEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (containsEvent[0]) {
                        containsEvent[0] = false;
                        user.getRelation("eventsAttending").remove(event);
                        event.getRelation("userRelation").remove(user);
                        binding.btFollowEvent.setText(R.string.follow_event);
                        binding.btFollowEvent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_24, 0, 0, 0);
                    } else {
                        user.getRelation("eventsAttending").add(event);
                        ParseRelation<ParseObject> usersFollowing = event.getRelation("userRelation");
                        usersFollowing.add(ParseUser.getCurrentUser());
                        binding.btFollowEvent.setText(R.string.unfollow_event);
                        binding.btFollowEvent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_do_not_disturb_24, 0, 0, 0);
                        containsEvent[0] = true;
                    }
                    try {
                        user.save();
                        event.save();
                    } catch (ParseException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            });
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
    }

    private Event queryEvent(String eventId) throws ParseException {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery("Event");
        eventQuery.include("creator");
        eventQuery.include("gym");
        return eventQuery.get(eventId);
    }

    private void queryUserEventsAttending() throws ParseException {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        eventQuery.whereEqualTo("userRelation", ParseUser.getCurrentUser());
        List<Event> userAttendingEvents = new ArrayList<>();
        userAttendingEvents = eventQuery.find();
        for (Event event : userAttendingEvents) {
            eventIds.add(event.getObjectId());
        }

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
