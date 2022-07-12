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

import com.example.indoor_volleyball.Activities.Details.GymDetailActivity;
import com.example.indoor_volleyball.Fragments.EventsFragment;
import com.example.indoor_volleyball.Fragments.GymFinderFragment;
import com.example.indoor_volleyball.Fragments.ProfileFragment;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    public void goToCreateEventFragment() {
        // switch the fragment being displayed
        binding.bottomNavigation.setSelectedItemId(R.id.events);
        //profilefragment.user = (User) user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                        fragment = new EventsFragment();
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
        binding.bottomNavigation.setSelectedItemId(R.id.events);
    }

    public void goToGymDetails(Gym gym) {
        Intent i = new Intent(this, GymDetailActivity.class);
        i.putExtra("gym", Parcels.wrap(gym));
        startActivity(i);
    }


}