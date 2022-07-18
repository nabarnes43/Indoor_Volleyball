package com.example.indoor_volleyball.Activities.Details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    ActivityGymDetailBinding binding;
    List<Gym> gymsFollowed;
    String gymId;
    private static final String GYM_ID_KEY = "gymId";
    public static Intent newIntent(Context context, String gymId) {
        Intent i = new Intent(context, GymDetailActivity.class);
        i.putExtra(GYM_ID_KEY, Parcels.wrap(gymId));
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
        gymsFollowed = new ArrayList<>();
        gymId = Parcels.unwrap(getIntent().getParcelableExtra(GYM_ID_KEY));
        try {
            queryGym(gymId);
        } catch (ParseException e) {
            Toast.makeText(this, "Gym Error", Toast.LENGTH_SHORT).show();
        }
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
            ParseUser creator;
            try {
                creator = findUsers(event.getCreator());
                ParseFile image = creator.getParseFile("profilePhoto");
                if (image != null) {
                    Glide.with(this).load(image.getUrl()).circleCrop().into(binding.itmEventItem.ivEventCreatorProfile);
                }
            } catch (ParseException e) {
                Toast.makeText(this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            binding.btViewMoreEvents.setText(R.string.no_events);
        }
        if (gym.getImage() != null) {
            Glide.with(this).load(gym.getImage().getUrl()).into(binding.ivGymPhotoDetail);
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
        binding.btFollowGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                ParseRelation<ParseObject> gymRelation = user.getRelation("gymsFollowing");
                gymRelation.add(gym);
                user.put("gymsFollowing", gymRelation);
                try {
                    user.save();
                    Toast.makeText(GymDetailActivity.this, gym.getObjectId() + " Followed", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ParseRelation<ParseObject> usersFollowing =  gym.getRelation("usersFollowing");
                usersFollowing.add(ParseUser.getCurrentUser());
                try {
                    gym.save();
                    Toast.makeText(GymDetailActivity.this, ParseUser.getCurrentUser().get("name") + " Added", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                }
            }
        });

    }

    private void queryGym(String gymId) throws ParseException {
        ParseQuery<Gym> gymQuery = ParseQuery.getQuery("Gym");
        gymQuery.whereEqualTo("objectId", gymId);
        gym = gymQuery.getFirst();
    }

    public void updateUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //Gets a list of all the gyms that the user follows.



            currentUser.put("", "new_email@example.com");

            // Saves the object.
            currentUser.saveInBackground(e -> {
                if(e==null){
                    //Save successfull
                    Toast.makeText(this, "Save Successful", Toast.LENGTH_SHORT).show();
                }else{
                    // Something went wrong while saving
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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