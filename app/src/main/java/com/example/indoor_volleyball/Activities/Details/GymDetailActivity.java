package com.example.indoor_volleyball.Activities.Details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.indoor_volleyball.Activities.CreateEventActivity;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.databinding.ActivityCreateGymBinding;
import com.example.indoor_volleyball.databinding.ActivityGymDetailBinding;
import com.parse.ParseException;

import org.parceler.Parcels;

public class GymDetailActivity extends AppCompatActivity {
    ActivityGymDetailBinding binding;
    public static final String TAG = "GymDetailActivity";
    Gym gym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGymDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        gym = Parcels.unwrap(getIntent().getParcelableExtra("gym"));

        binding.tvGymNameDetail.setText(gym.getName());
        binding.rbGymRatingDetail.setRating(gym.getRating().floatValue());
        //TODO resource strings
//        try {
//            binding.tvNextEventsDetail.setText("Start Time: " + gym.getNextEvent().getStartTime() + " End Time " + gym.getNextEvent().getEndTime() +
//                    " Max Count " + gym.getNextEvent().getMaxCount() +" Skill Level: " + gym.getNextEvent().getSkillLevel()+ " Details: " + gym.getNextEvent().getDetails());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        binding.fabCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateEvent(gym);
            }
        });

    }

    private void goToCreateEvent(Gym gym) {
        String gymId = gym.getObjectId();
        Intent i = new Intent(this, CreateEventActivity.class);
        i.putExtra("gymId", Parcels.wrap(gymId));
        startActivity(i);

    }



}