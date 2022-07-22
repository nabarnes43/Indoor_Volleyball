package com.example.indoor_volleyball.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityCreateEventBinding;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateEventActivity extends AppCompatActivity {
    private static final String TAG = "CreateEventActivity";
    private Date startTime;
    private Date endTime;
    private Gym thisGym;
    private String skillLevel;
    private Boolean allowPlusOnes;
    private Boolean allowSpectators;
    private Calendar date;
    private static final String GYM_ID_KEY = "gymId";

    public static Intent newIntent(Context context, String gymId) {
        Intent i = new Intent(context, CreateEventActivity.class);
        i.putExtra(GYM_ID_KEY, Parcels.wrap(gymId));
        return i;
    }

    //TODO make this page Multi purpose for edit.

    //Todo check boxes and spinner formatting.
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(("M-dd-yyyy hh:mm:ss a"), Locale.US);
    private Boolean startTimeTrue;
    private ActivityCreateEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String thisGymId = Parcels.unwrap(getIntent().getParcelableExtra(GYM_ID_KEY));
        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        queryGym(thisGymId);
        skillLevel();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        binding.tvStartTime.setInputType(InputType.TYPE_NULL);
        binding.tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimeTrue = true;
                showDateTimePicker();
            }
        });
        binding.tvStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    startTimeTrue = true;
                    showDateTimePicker();
                }
            }
        });
        binding.tvEndTime.setInputType(InputType.TYPE_NULL);
        binding.tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimeTrue = false;
                showDateTimePicker();
            }
        });
        binding.tvEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    startTimeTrue = false;
                    showDateTimePicker();
                }
            }
        });
        //TODO create error detection.
        binding.btCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllowPlusOnesSpectators();
                int minPlayers = Integer.parseInt(binding.etMinPlayers.getText().toString());
                int maxPlayers = Integer.parseInt(binding.etMaxPlayers.getText().toString());
                String details = binding.etDetails.getText().toString();
                String teamRotation = binding.etTeamRotation.getText().toString();
                String eventCode = binding.etEventCode.getText().toString();
                Event event = new Event();
                event.setStartTime(startTime);
                event.setEndTime(endTime);
                event.setMinCount(minPlayers);
                event.setMaxCount(maxPlayers);
                event.setSkillLevel(skillLevel);
                event.setAllowPlusOnes(allowPlusOnes);
                event.setAllowSpectators(allowSpectators);
                event.setDetails(details);
                event.setEventCode(eventCode);
                event.setWaitList(0);
                event.setGym(thisGym);
                event.setCreator(ParseUser.getCurrentUser());
                event.setTeamRotation(teamRotation);
                try {
                    event.save();
                    Log.i(TAG, getString(R.string.save_succeeded_text));
                    Toast.makeText(CreateEventActivity.this, getString(R.string.save_succeeded_text), Toast.LENGTH_SHORT).show();
                    sendNotification(thisGym);
                    try {
                        queryNextEventAtGym(thisGym);
                    } catch (ParseException z) {
                        Toast.makeText(CreateEventActivity.this, z.toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setAllowPlusOnesSpectators() {
        if (binding.cbAllowSpectators.isChecked()) {
            allowSpectators = true;
        }
        if (binding.cbAllowPlusOnes.isChecked()) {
            allowPlusOnes = true;
        }
    }

    //TODO buggy sends to both accounts I think due to login history.
    public void sendNotification(Gym gym) throws ParseException {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("gymsFollowing", gym);
        // Find devices associated with these users
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery("user", userQuery);
        JSONObject data = new JSONObject();
        // Put data in the JSON object
        try {
            data.put("alert", "New event created at " + gym.getName());
            data.put("title", "New Event!");
        } catch (JSONException e) {
            // should not happen
            throw new IllegalArgumentException("unexpected parsing error", e);
        }
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setChannel("Events");
        push.setData(data);
        push.sendInBackground();
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

    private void queryGym(String gymId) {
        ParseQuery<Gym> gymQuery = ParseQuery.getQuery(getString(R.string.query_gym_text));
        gymQuery.getInBackground(gymId, (gym, e) -> {
            if (e == null) {
                thisGym = gym;
                Objects.requireNonNull(getSupportActionBar()).setTitle(thisGym.getName());
            } else {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    private void queryNextEventAtGym(Gym gym) throws ParseException {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        eventQuery.whereEqualTo("gym", gym);
        //Todo need to delete old events and empty gyms
        eventQuery.whereLessThan("startTime", Calendar.getInstance().getTime());
        eventQuery.whereGreaterThanOrEqualTo("startTime", startTime);
        eventQuery.orderByAscending("startTime");
        Event nextEvent = eventQuery.getFirst();
        Toast.makeText(this, "next event details " + nextEvent.getDetails(), Toast.LENGTH_SHORT).show();
        thisGym.setNextEvent(nextEvent);
        thisGym.save();
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        if (startTimeTrue) {
                            startTime = date.getTime();
                            binding.tvStartTime.setText(dateFormat.format(date.getTime()));
                        } else {
                            endTime = date.getTime();
                            binding.tvEndTime.setText(dateFormat.format(date.getTime()));
                        }
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();

    }

    private void skillLevel() {
        //create a list of items for the spinner.
        String[] items = getResources().getStringArray(R.array.skill_choices);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        binding.spSkillLevel.setAdapter(adapter);
        binding.spSkillLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skillLevel = parent.getItemAtPosition(position).toString();
                binding.tvSkillChangedText.setText(skillLevel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}