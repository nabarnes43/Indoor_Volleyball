package com.example.indoor_volleyball.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.databinding.ActivityQueryBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class QueryActivity extends AppCompatActivity {

    private static final String TAG = "QueryActivity";
    private ActivityQueryBinding binding;
    private List<Gym> allGyms;
    private List<Gym> allGymsByDistance;
    private List<Gym> allGymsWithinDistance;
    private List<Gym> gymsFollowed;
    private List<Event> allEvents;
    private List<Event> userAttending;
    private List<Event> userManaging;
    private List<Event> eventsAtGymSortedByDateDescending;
    private List<ParseUser> allUsersAttendingEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allGyms = new ArrayList<>();
        allGymsByDistance = new ArrayList<>();
        allGymsWithinDistance = new ArrayList<>();
        gymsFollowed = new ArrayList<>();
        allEvents = new ArrayList<>();
        userAttending = new ArrayList<>();
        userManaging = new ArrayList<>();
        allUsersAttendingEvent = new ArrayList<>();
        eventsAtGymSortedByDateDescending = new ArrayList<>();

        binding = ActivityQueryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            queryAllEvents();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            queryAllGyms();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        queryUserEventsAttending();
        queryUserEventsManaging();
        queryUserGyms();
        queryAllUsersAttendingEvent(allEvents.get(1));
        queryEventsAtGym(allGyms.get(0));
        try {
            queryNextEventAtGym(allGyms.get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        queryUsersGymsByDistance(ParseUser.getCurrentUser());
        allGymsByDistance(ParseUser.getCurrentUser().getParseGeoPoint("longLat"));
        allGymsWithinDistanceOfUser(ParseUser.getCurrentUser().getParseGeoPoint("longLat"), 10.0);
        binding.btLogOut.setOnClickListener(v -> ParseUser.logOutInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Error signing out", e);
                Toast.makeText(QueryActivity.this, "Error signing out", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.i(TAG, "Sign out successful");
            goToLoginActivity();
            Toast.makeText(QueryActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
        }));
        binding.btGymsFollowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Gym gym : gymsFollowed) {
                    String gymName = gym.getName();
                    Number gymRating = gym.getRating();
                    ParseGeoPoint gymLocation = gym.getLocation();
                    String original_text = binding.tvGymsFollowed.getText().toString();
                    original_text = original_text + "Name: " + gymName + "\n Rating:" + gymRating + "\n Location :" + gymLocation + "\n\n";
                    binding.tvGymsFollowed.setText(original_text);
                }
            }
        });
        binding.btAllGymsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Gym gym : allGyms) {
                    String gymName = gym.getName();
                    Number gymRating = gym.getRating();
                    ParseGeoPoint gymLocation = gym.getLocation();
                    String original_text = binding.tvAllGyms.getText().toString();
                    original_text = original_text + "Name: " + gymName + "\n Rating:" + gymRating + "\n Location :" + gymLocation + "\n\n";
                    binding.tvAllGyms.setText(original_text);
                }
            }
        });
        binding.btAllGymsByDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Gym gym : allGymsByDistance) {
                    String gymName = gym.getName();
                    Number gymRating = gym.getRating();
                    ParseGeoPoint gymLocation = gym.getLocation();
                    String original_text = binding.tvAllGymsByDistance.getText().toString();
                    original_text = original_text + "Name: " + gymName + "\n Rating:" + gymRating + "\n Location :" + gymLocation + "\n\n";
                    binding.tvAllGymsByDistance.setText(original_text);
                }
            }
        });
        binding.btAllGymsWithinDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Gym gym : allGymsWithinDistance) {
                    String gymName = gym.getName();
                    Number gymRating = gym.getRating();
                    ParseGeoPoint gymLocation = gym.getLocation();
                    String original_text = binding.tvAllGymsWithinDistance.getText().toString();
                    original_text = original_text + "Name: " + gymName + "\n Rating:" + gymRating + "\n Location :" + gymLocation + "\n\n";
                    binding.tvAllGymsWithinDistance.setText(original_text);
                }
            }
        });

        binding.btAllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Event event : allEvents) {
                    Gym gym = event.getGym();
                    String gymName = gym.getName();
                    String eventDescription = event.getDetails();
                    ParseGeoPoint gymLocation = gym.getLocation();
                    Date startTime = event.getStartTime();
                    Date endTime = event.getEndTime();
                    String original_text = binding.tvAllEvents.getText().toString();
                    original_text = original_text + "Name: " + gymName + "\n Details: " + eventDescription + "\n Location :" + gymLocation + "Time: " + startTime + "-" + endTime + "\n\n";
                    binding.tvAllEvents.setText(original_text);
                }
            }
        });

        binding.btUserAttending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Event event : userAttending) {
                    Gym gym = event.getGym();
                    String gymName = gym.getName();
                    String eventDescription = event.getDetails();
                    ParseGeoPoint gymLocation = gym.getLocation();
                    Date startTime = event.getStartTime();
                    Date endTime = event.getEndTime();
                    String original_text = binding.tvUserAttending.getText().toString();
                    original_text = original_text + "Name: " + gymName + "\n Details: " + eventDescription + "\n Location :" + gymLocation + "Time: " + startTime + "-" + endTime + "\n\n";
                    binding.tvUserAttending.setText(original_text);
                }
            }
        });

        binding.btUserManaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Event event : userManaging) {
                    Gym gym = event.getGym();
                    String gymName = gym.getName();
                    String eventDescription = event.getDetails();
                    ParseGeoPoint gymLocation = gym.getLocation();
                    Date startTime = event.getStartTime();
                    Date endTime = event.getEndTime();
                    String original_text = binding.tvUserManaging.getText().toString();
                    original_text = original_text + "Name: " + gymName + "\n Details: " + eventDescription + "\n Location :" + gymLocation + "Time: " + startTime + "-" + endTime + "\n\n";
                    binding.tvUserManaging.setText(original_text);
                }
            }
        });

        binding.btUsersAttendingEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ParseUser user : allUsersAttendingEvent) {
                    String userName = user.getString("name");
                    String userSkill = user.getString("skillLevel");
                    String phoneNumber = user.getString("phoneNumber");
                    String original_text = binding.tvUsersAttending.getText().toString();
                    original_text = original_text + "Name: " + userName + "\n Skill Level: " + userSkill + "\n Phone Number :" + phoneNumber + "\n\n";
                    binding.tvUsersAttending.setText(original_text);
                }
            }
        });

        binding.btEventsAtGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Event event : eventsAtGymSortedByDateDescending) {
                    Gym gym = event.getGym();
                    String gymName = gym.getName();
                    String eventDescription = event.getDetails();
                    ParseGeoPoint gymLocation = gym.getLocation();
                    Date startTime = event.getStartTime();
                    Date endTime = event.getEndTime();
                    String original_text = binding.tvEventsAtGym.getText().toString();
                    original_text = original_text + "Name: " + gymName + "\n Details: " + eventDescription + "\n Location :" + gymLocation + "Time: " + startTime + "-" + endTime + "\n\n";
                    binding.tvEventsAtGym.setText(original_text);
                }
            }
        });


    }


    //Gets a list of all the gyms in the Database gets the info gym maps page.
    private void queryAllGyms() throws ParseException {
        ParseQuery<Gym> query = ParseQuery.getQuery(Gym.class);
        allGyms.addAll(query.find());
    }

    //Gets a list of all the gyms in order of distance from the user.
    private void allGymsByDistance(ParseGeoPoint userLocation) {
        ParseQuery<Gym> query = new ParseQuery<>("Gym");
        query.whereNear("location", userLocation);
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymList, ParseException e) {
                if (e == null) {
                    allGymsByDistance.addAll(gymList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    //Gets a list of all the gyms within a given distance of the user. TODO within miles
    private void allGymsWithinDistanceOfUser(ParseGeoPoint userLocation, Double miles) {
        ParseQuery<Gym> query = new ParseQuery<>("Gym");
        query.whereWithinKilometers("location", new ParseGeoPoint(userLocation), miles * 1.60934);
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymList, ParseException e) {
                if (e == null) {
                    allGymsWithinDistance.addAll(gymList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    //Get a list of all events
    private void queryAllEvents() throws ParseException {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include("gym");
        //Use this when you are getting null pointers. The nulls mean find in background is to slow.
        allEvents.addAll(query.find());
    }

    //Get a list of all the events at the given gym sorted by Date Descending.
    private void queryEventsAtGym(Gym gym) {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        eventQuery.whereEqualTo("gym", gym);
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> eventList, ParseException e) {
                if (e == null) {
                    Collections.sort(eventList, new Comparator<Event>() {
                        public int compare(Event o1, Event o2) {
                            return o1.getStartTime().compareTo(o2.getStartTime());
                        }
                    });
                    eventsAtGymSortedByDateDescending.addAll(eventList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void queryNextEventAtGym(Gym gym) throws ParseException {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        eventQuery.whereEqualTo("gym", gym);
        eventQuery.orderByAscending("startTime");
        eventQuery.setLimit(1);
        List<Event> nextEventList = new ArrayList<>();
        nextEventList.addAll(eventQuery.find());
        //nextEvent = nextEventList.get(0);
        gym.setNextEvent(nextEventList.get(0));
    }

    //Gets a list of all the gyms that the user follows.
    private void queryUserGyms() {
        ParseUser user = ParseUser.getCurrentUser();
        gymsFollowed.addAll(user.getList("gymsFollowed"));
    }

    //Gets a list of all the events that a user has added themselves to.
    private void queryUserEventsAttending() {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        eventQuery.whereEqualTo("userRelation", ParseUser.getCurrentUser());
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> eventList, ParseException e) {
                if (e == null) {
                    userAttending.addAll(eventList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    //Gets a list of all the events that a user is managing.
    private void queryUserEventsManaging() {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        eventQuery.whereEqualTo("creator", ParseUser.getCurrentUser());
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> eventList, ParseException e) {
                if (e == null) {
                    userManaging.addAll(eventList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    //Gets a list of all the users attending an event. Make sure that the relation goes both ways. If you call this and the user dos not have the event only the other way around it wont run.
    private void queryAllUsersAttendingEvent(Event event) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("eventRelation", event);
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null) {
                    allUsersAttendingEvent.addAll(userList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    //Get a list of gyms that the user follows.
    private void queryUsersGymsByDistance(ParseUser user) {
        ParseGeoPoint userLocation = user.getParseGeoPoint("longLat");
        ParseQuery<Gym> query = new ParseQuery<>("Gym");
        query.whereEqualTo("usersFollowing", user);
        query.whereNear("location", userLocation);
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymList, ParseException e) {
                for (Gym gym : gymList) {
                    Toast.makeText(QueryActivity.this, "Gym name: " + gym.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Logout User
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}