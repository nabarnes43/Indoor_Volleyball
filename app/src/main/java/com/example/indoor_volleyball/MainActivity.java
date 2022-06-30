package com.example.indoor_volleyball;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.indoor_volleyball.Fragments.EventsFragment;
import com.example.indoor_volleyball.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

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

        binding.btQueryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToQueryActivity();
            }
        });

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
                        fragment = new EventsFragment();
                        break;
                    case R.id.profile:
                        // do something here
                        Toast.makeText(MainActivity.this, "PROFILE", Toast.LENGTH_SHORT).show();
                        fragment = new EventsFragment();
                        break;
                    default: return false;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        //Default
        binding.bottomNavigation.setSelectedItemId(R.id.events);


        binding.btLogOut.setOnClickListener(v -> ParseUser.logOutInBackground(e -> {

            if(e != null) {
                Log.e(TAG, "Error signing out", e);
                Toast.makeText(MainActivity.this, "Error signing out", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.i(TAG, "Sign out successful");
            goToLoginActivity();
            Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
        }));

    }



    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void gotToQueryActivity() {
        Intent i = new Intent(this, QueryActivity.class);
        startActivity(i);
    }
}