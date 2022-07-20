package com.example.indoor_volleyball.Activities;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.indoor_volleyball.EventTodayReminderWorker;
import com.example.indoor_volleyball.Fragments.EventFragments.EventFinderFragment;
import com.example.indoor_volleyball.Fragments.GymFragments.GymFinderFragment;
import com.example.indoor_volleyball.Fragments.ProfileFragment;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

// Java Dependencies
import java.util.HashMap; // This includes the HasMap Object that the Cloud function needs to call
// Parse Dependencies
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String TAGW = "Event Today";
    private static final String CHANNEL_ID = "Events";
    private ActivityMainBinding binding;
    public static LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        createNotificationChannel();
        View view = binding.getRoot();
        setContentView(view);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        PeriodicWorkRequest uploadWorkRequest =
                new PeriodicWorkRequest.Builder(EventTodayReminderWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                        .addTag("Notify Event Today")
                        .build();
        createNotificationChannel();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(TAGW, ExistingPeriodicWorkPolicy.KEEP, uploadWorkRequest);

        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.events:
                        fragment = new EventFinderFragment();
                        break;
                    case R.id.find:
                        fragment = new GymFinderFragment();
                        break;
                    case R.id.profile:
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

        final HashMap<String, String> params = new HashMap<>();
        // Calling the cloud code function
        ParseCloud.callFunctionInBackground("pushsample", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object response, ParseException exc) {
                if(exc == null) {
                    // The function was executed, but it's interesting to check its response
                    alertDisplayer("Successful Push","Check on your phone the notifications to confirm!");
                }
                else {
                    // Something went wrong
                    Toast.makeText(MainActivity.this, exc.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My notification name";
            String description = "My notification description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}