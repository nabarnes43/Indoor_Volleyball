package com.example.indoor_volleyball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.databinding.ActivityQueryBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class QueryActivity extends AppCompatActivity {

    private static final String TAG = "QueryActivity";
    private ActivityQueryBinding binding;
    List<Gym> allGyms;
    List<Gym> gymsFollowed;
    List<Event> allEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        allGyms = new ArrayList<>();
        gymsFollowed = new ArrayList<>();
        allEvents = new ArrayList<>();

        binding = ActivityQueryBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

        binding.tvQuery.setText("");
        try {
            queryAllEvents();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        queryAllGyms();
        queryUserEventsAttending();
        queryUserEventsManaging();
        queryUserGyms();


        queryAllUsersAttendingEvent(allEvents.get(0));
        //Toast.makeText(QueryActivity.this, "Deatais: " + allEvents.get(0).getDetails(), Toast.LENGTH_SHORT).show();


        binding.btLogOut.setOnClickListener(v -> ParseUser.logOutInBackground(e -> {

            if(e != null) {
                Log.e(TAG, "Error signing out", e);
                Toast.makeText(QueryActivity.this, "Error signing out", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.i(TAG, "Sign out successful");
            goToLoginActivity();
            Toast.makeText(QueryActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
        }));

        binding.btAllGymsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Gym gym: allGyms) {
                    String gymName = gym.getName();
                    int gymRating = gym.getRating();
                    ParseGeoPoint gymLocation = gym.getLocation();
                    String original_text = binding.tvAllGyms.getText().toString();
                    original_text = original_text + "Name: "+gymName + "\n Rating:" + gymRating+"\n Location :"+ gymLocation +"\n\n";
                    binding.tvAllGyms.setText(original_text);
                }
            }
        });

        binding.btGymsFollowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Gym gym: gymsFollowed) {
                    String gymName = gym.getName();
                    int gymRating = gym.getRating();
                    ParseGeoPoint gymLocation = gym.getLocation();
                    String original_text = binding.tvGymsFollowed.getText().toString();
                    original_text = original_text + "Name: "+gymName + "\n Rating:" + gymRating+"\n Location :"+ gymLocation +"\n\n";
                    binding.tvGymsFollowed.setText(original_text);
                }
            }
        });


    }


    //Gets a list of all the gyms in the Database.
    private void queryAllGyms() {
        ParseQuery<Gym> query = ParseQuery.getQuery(Gym.class);

        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }

                allGyms.addAll(gymList);

            }
        });
    }

    //Get a list of all events
    private void queryAllEvents() throws ParseException {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);

        // Execute the find asynchronously
        allEvents.addAll(query.find());
        /*
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> eventList, ParseException e) {
                if (e == null) {
                    // Access the array of results here


                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
                allEvents.addAll(eventList);
                String eventDetails = allEvents.get(0).getDetails();
                Toast.makeText(QueryActivity.this, "Details: " + eventDetails, Toast.LENGTH_LONG).show();


            }
        });

         */
    }
    //Gets a list of all the gyms that the user follows.
    private void queryUserGyms(){
        ParseUser user = ParseUser.getCurrentUser();
        gymsFollowed.addAll(user.getList("gymsFollowed"));
    }
    //Gets a list of all the events that a user has added themselves to.
    private void queryUserEventsAttending() {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        eventQuery.whereEqualTo("userRelation", ParseUser.getCurrentUser());
        // Execute the find asynchronously
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> eventList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    for (Event event: eventList) {
                        Gym eventGym = (Gym) event.getGym();
                        String gymName = eventGym.getName();
                        int gymRating = eventGym.getRating();
                        ParseGeoPoint gymLocation = eventGym.getLocation();
                        //Toast.makeText(QueryActivity.this, "Name: "+gymName + "\n Rating:" + gymRating+" Location :"+ gymLocation, Toast.LENGTH_LONG).show();
                    }
                    //allGyms.addAll(gymList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }


            }
        });
    }

    //Gets a list of all the events that a user has added themselves to.
    private void queryUserEventsManaging() {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        eventQuery.whereEqualTo("creator", ParseUser.getCurrentUser());
        // Execute the find asynchronously
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> eventList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    for (Event event: eventList) {
                        Gym eventGym = (Gym) event.getGym();
                        String gymName = eventGym.getName();
                        int gymRating = eventGym.getRating();
                        ParseGeoPoint gymLocation = eventGym.getLocation();
                        //Toast.makeText(QueryActivity.this, "Name: "+gymName + "\n Rating:" + gymRating+" Location :"+ gymLocation, Toast.LENGTH_LONG).show();
                    }
                    //allGyms.addAll(gymList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }
    //Gets a list of all the users attending an event
    private void queryAllUsersAttendingEvent(Event event) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("eventRelation", event);

        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    for (ParseUser user : userList) {

                        String userName = user.getString("name");
                        String userSkillLevel = user.getString("skillLevel");
                        ParseGeoPoint userLocation = user.getParseGeoPoint("longLat");
                        Toast.makeText(QueryActivity.this, "Name: " + userName + "\n Rating:" + userSkillLevel + " Location :" + userLocation, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }











    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }







}