package com.example.indoor_volleyball.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.indoor_volleyball.Activities.Details.EventAttendingActivity;
import com.example.indoor_volleyball.Activities.Details.EventCreatorDetailActivity;
import com.example.indoor_volleyball.Activities.Details.GymDetailActivity;
import com.example.indoor_volleyball.Fragments.EventFragments.EventFinderFragment;
import com.example.indoor_volleyball.Fragments.GymFragments.GymFinderFragment;
import com.example.indoor_volleyball.Fragments.ProfileFragment;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);
        final FragmentManager fragmentManager = getSupportFragmentManager();


        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.events:
                        // do something here
                        Toast.makeText(MainActivity.this, "EVENTS", Toast.LENGTH_SHORT).show();
                        fragment = new EventFinderFragment();
                        break;
                    case R.id.find:
                        // do something here
                        Toast.makeText(MainActivity.this, "FIND", Toast.LENGTH_SHORT).show();
                        fragment = new GymFinderFragment();
                        break;
                    case R.id.profile:
                        // do something here
                        Toast.makeText(MainActivity.this, "PROFILE", Toast.LENGTH_SHORT).show();
                        fragment = new ProfileFragment();
                        break;
                    default:
                        return false;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        //Default
        binding.bottomNavigation.setSelectedItemId(R.id.find);
    }

    public void goToGymDetails(Gym gym) {
        Intent i = new Intent(this, GymDetailActivity.class);
        i.putExtra("gym", Parcels.wrap(gym));
        startActivity(i);
    }

    public void goToEventDetailsAttending(Event event) {
        Intent i = new Intent(this, EventAttendingActivity.class);
        i.putExtra("event", Parcels.wrap(event));
        startActivity(i);
    }

    public void goToEventDetailsCreating(Event event) {
        Intent i = new Intent(this, EventCreatorDetailActivity.class);
        i.putExtra("eventId", Parcels.wrap(event.getObjectId()));
        startActivity(i);
    }


}