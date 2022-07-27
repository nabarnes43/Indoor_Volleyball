package com.example.indoor_volleyball.Activities.Details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.indoor_volleyball.Activities.CreateEventActivity;
import com.example.indoor_volleyball.Activities.EventsAtGymActivity;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityGymDetailBinding;
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

public class GymDetailActivity extends AppCompatActivity {
    private static final String TAG = "GymDetailActivity";
    private ActivityGymDetailBinding binding;
    private List<Gym> gymsFollowed;
    private String gymId;
    private List<String> gymsFollowingIds;
    private Gym gym;
    private static final String GYM_ID_KEY = "gymId";

    public static Intent newIntent(Context context, String gymId) {
        Intent i = new Intent(context, GymDetailActivity.class);
        i.putExtra(GYM_ID_KEY, Parcels.wrap(gymId));
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGymDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gymsFollowed = new ArrayList<>();
        gymsFollowingIds = new ArrayList<>();
        gymId = Parcels.unwrap(getIntent().getParcelableExtra(GYM_ID_KEY));
        try {
            queryGymsFollowing();
            queryGym(gymId);
        } catch (ParseException e) {
            Toast.makeText(this, "Gym Error", Toast.LENGTH_SHORT).show();
        }
        binding.rbGymRatingDetail.setRating(gym.getRating().floatValue());
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
            ParseUser creator;
            try {
                creator = findUsers(event.getCreator());
                ParseFile image = creator.getParseFile("profilePhoto");
                if (image != null) {
                    Glide.with(this).load(image.getUrl()).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.itmEventItem.ivEventCreatorProfile);
                }
            } catch (ParseException e) {
                Toast.makeText(this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
            }
            Event finalEvent = event;
            binding.itmEventItem.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalEvent.getCreator().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        goToEventDetailsCreating(GymDetailActivity.this, finalEvent);
                    } else {
                        goToEventDetailsAttending(GymDetailActivity.this, finalEvent);
                    }
                }
            });
        } else {
            binding.btViewMoreEvents.setText(R.string.no_events);
        }
        if (gym.getImage() != null) {
            Glide.with(this).load(gym.getImage().getUrl()).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.ivGymPhotoDetail);
        } else {
            Glide.with(this).load(R.drawable.icon_gym_black).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.ivGymPhotoDetail);
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
        ParseUser user = ParseUser.getCurrentUser();
        final boolean[] containsEvent = new boolean[1];

        if (gymsFollowingIds.contains(gymId)) {
            containsEvent[0] = true;
            binding.btFollowGym.setText(R.string.unfollow_event);
            binding.btFollowGym.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_do_not_disturb_24_white, 0, 0, 0);
        } else {
            containsEvent[0] = false;
            binding.btFollowGym.setText(R.string.follow_event);
            binding.btFollowGym.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_24_white, 0, 0, 0);
        }
        binding.btFollowGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (containsEvent[0]) {
                    containsEvent[0] = false;
                    user.getRelation("gymsFollowing").remove(gym);
                    gym.getRelation("usersFollowing").remove(user);
                    binding.btFollowGym.setText(R.string.follow_gym);
                    binding.btFollowGym.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_24_white, 0, 0, 0);
                } else {
                    user.getRelation("gymsFollowing").add(gym);
                    ParseRelation<ParseObject> usersFollowing = gym.getRelation("usersFollowing");
                    usersFollowing.add(ParseUser.getCurrentUser());
                    binding.btFollowGym.setText(R.string.unfollow_gym);
                    binding.btFollowGym.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_do_not_disturb_24_white, 0, 0, 0);
                    containsEvent[0] = true;
                }
                try {
                    user.save();
                    gym.save();
                } catch (ParseException e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    private void queryGymsFollowing() throws ParseException {
                ParseQuery<Gym> gymQuery = ParseQuery.getQuery(Gym.class);
                gymQuery.whereEqualTo("usersFollowing", ParseUser.getCurrentUser());
                List<Gym> userFollowingGyms = new ArrayList<>();
                userFollowingGyms = gymQuery.find();
                for (Gym gym : userFollowingGyms) {
                    gymsFollowingIds.add(gym.getObjectId());
                }
            }

    public void goToEventDetailsAttending(Context context, Event event) {
        String eventId = event.getObjectId();
        Intent i = EventAttendingActivity.newIntent(context, eventId);
        context.startActivity(i);
    }

    public void goToEventDetailsCreating(Context context, Event event) {
        String eventId = event.getObjectId();
        Intent i = CreateEventActivity.newIntentEvent(context, eventId);
        context.startActivity(i);
    }

    private void queryGym(String gymId) throws ParseException {
        ParseQuery<Gym> gymQuery = ParseQuery.getQuery("Gym");
        gymQuery.whereEqualTo("objectId", gymId);
        gym = gymQuery.getFirst();
    }

    public ParseUser findUsers(ParseUser user) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", user.getObjectId());
        return query.getFirst();
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
        Intent i = CreateEventActivity.newIntent(this, gymId);
        startActivity(i);
    }

    private void goToEventsAtGym(Gym gym) {
        Intent i = EventsAtGymActivity.newIntent(this, gym);
        startActivity(i);
    }
}