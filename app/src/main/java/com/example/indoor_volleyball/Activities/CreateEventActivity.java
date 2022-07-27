package com.example.indoor_volleyball.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityCreateEventBinding;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
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
    private String skillLevel = "C";
    private Boolean allowPlusOnes = false;
    private Boolean allowSpectators = false;
    private Calendar date;
    private Event thisEvent;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(("MMMM dd hh:mm a"), Locale.US);
    private Boolean startTimeTrue;
    String thisGymId;
    private ActivityCreateEventBinding binding;
    private static final String GYM_ID_KEY = "gymId";
    private static final String EVENT_ID_KEY = "eventId";

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // get the content of both the edit text
            String startTime = binding.tvStartTime.getText().toString();
            String endTime = binding.tvEndTime.getText().toString();
            String minPlayers = binding.etMinPlayers.getText().toString();
            String maxPlayers = binding.etMaxPlayers.getText().toString();
            String details = binding.etDetails.getText().toString();
            String eventCode = binding.etEventCode.getText().toString();
            String rotationRule = binding.etTeamRotation.getText().toString();
            // check whether both the fields are empty or not
            binding.btCreateEvent.setEnabled(!startTime.isEmpty() && !endTime.isEmpty() && !minPlayers.isEmpty()
                    && !maxPlayers.isEmpty() && !details.isEmpty() && !eventCode.isEmpty() && !rotationRule.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static Intent newIntent(Context context, String gymId) {
        Intent i = new Intent(context, CreateEventActivity.class);
        i.putExtra(GYM_ID_KEY, Parcels.wrap(gymId));
        return i;
    }

    public static Intent newIntentEvent(Context context, String eventId) {
        Intent i = new Intent(context, CreateEventActivity.class);
        i.putExtra(EVENT_ID_KEY, Parcels.wrap(eventId));
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String thisGymId = Parcels.unwrap(getIntent().getParcelableExtra(GYM_ID_KEY));
        String thisEventId = Parcels.unwrap(getIntent().getParcelableExtra(EVENT_ID_KEY));
        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (thisGymId != null) {
            queryGym(thisGymId);
        } else {
            queryEvent(thisEventId);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        if (thisGymId != null) {
            binding.tvStartTime.addTextChangedListener(textWatcher);
            binding.tvEndTime.addTextChangedListener(textWatcher);
            binding.etMaxPlayers.addTextChangedListener(textWatcher);
            binding.etMinPlayers.addTextChangedListener(textWatcher);
            binding.etDetails.addTextChangedListener(textWatcher);
            binding.etEventCode.addTextChangedListener(textWatcher);
            binding.etTeamRotation.addTextChangedListener(textWatcher);
        }
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
        binding.btCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllowPlusOnesSpectators();
                int minPlayers;
                int maxPlayers;
                String details;
                String teamRotation;
                String eventCode;
                Event event = new Event();
                if (thisGymId == null) {
                    event = thisEvent;
                    skillLevel = thisEvent.getSkillLevel();
                    thisGym = thisEvent.getGym();
                }
                if (binding.tvStartTime.getText().toString().isEmpty()) {
                        startTime = thisEvent.getStartTime();
                }
                if (binding.tvEndTime.getText().toString().isEmpty()) {
                    endTime = thisEvent.getEndTime();
                }
                if (binding.etMinPlayers.getText().toString().isEmpty()) {
                    minPlayers = (int) thisEvent.getMinCount();
                } else {
                    minPlayers = Integer.parseInt(binding.etMinPlayers.getText().toString());
                }
                if (binding.etMaxPlayers.getText().toString().isEmpty()) {
                    maxPlayers = (int) thisEvent.getMaxCount();
                } else {
                    maxPlayers = Integer.parseInt(binding.etMaxPlayers.getText().toString());
                }
                if (binding.etDetails.getText().toString().isEmpty()) {
                    details = thisEvent.getDetails();
                } else {
                    details = binding.etDetails.getText().toString();
                }
                if (binding.etTeamRotation.getText().toString().isEmpty()) {
                    teamRotation = thisEvent.getTeamRotation();
                } else {
                    teamRotation = binding.etTeamRotation.getText().toString();
                }
                if (binding.etEventCode.getText().toString().isEmpty()) {
                    eventCode = thisEvent.getEventCode();
                } else {
                    eventCode = binding.etEventCode.getText().toString();
                }
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

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setTextSize(20);
        spinner.setItems(getResources().getStringArray(R.array.skill_choices));
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setEventData() {
        String startTimeText = getString(R.string.event_get_start_time_text) + thisEvent.getStartTime().toString();
        String endTimeText = getString(R.string.event_get_end_time_text) + thisEvent.getEndTime().toString();
        String minPlayersText = getString(R.string.event_min_players_text) + thisEvent.getMinCount().toString();
        String maxPlayersText = getString(R.string.event_get_max_players_text) + thisEvent.getMaxCount().toString();
        String skillLevelText = getString(R.string.event_get_skill_level_text) + thisEvent.getSkillLevel();
        String detailsText = getString(R.string.event_get_details_text) + thisEvent.getDetails();
        String eventCodeText = getString(R.string.event_get_event_code_text) + thisEvent.getEventCode();
        String rotationText = getString(R.string.event_get_rotation_text) + thisEvent.getTeamRotation();
        binding.tvCreateEvent.setText(R.string.event_creator_manage_event_text);
        binding.tvStartTime.setHint(startTimeText);
        binding.tvEndTime.setHint(endTimeText);
        binding.etMinPlayers.setHint(minPlayersText);
        binding.etMaxPlayers.setHint(maxPlayersText);
        binding.etDetails.setHint(skillLevelText);
        binding.cbAllowPlusOnes.setChecked(thisEvent.getAllowPlusOnes());
        binding.cbAllowSpectators.setChecked(thisEvent.getAllowPlusOnes());
        binding.etDetails.setHint(detailsText);
        binding.etEventCode.setHint(eventCodeText);
        binding.etTeamRotation.setHint(rotationText);
        binding.btCreateEvent.setText(R.string.save_changes_buttom);
        binding.spinner.setText(thisEvent.getSkillLevel());
    }

    public void setAllowPlusOnesSpectators() {
        if (binding.cbAllowSpectators.isChecked()) {
            allowSpectators = true;
        }
        if (binding.cbAllowPlusOnes.isChecked()) {
            allowPlusOnes = true;
        }
    }

    public void sendNotification(Gym gym) throws ParseException {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("gymsFollowing", gym);
        // Find devices associated with these users
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery("user", userQuery);
        JSONObject data = new JSONObject();
        // Put data in the JSON object
        if (thisGymId == null) {
            try {
                data.put("alert", "Event updated at " + gym.getName());
                data.put("title", "Event Update!");
            } catch (JSONException e) {
                // should not happen
                throw new IllegalArgumentException("unexpected parsing error", e);
            }
        } else {
            try {
                data.put("alert", "New event created at " + gym.getName());
                data.put("title", "New Event!");
            } catch (JSONException e) {
                // should not happen
                throw new IllegalArgumentException("unexpected parsing error", e);
            }
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
        ParseQuery<Gym> gymQuery = ParseQuery.getQuery("Gym");
        gymQuery.getInBackground(gymId, (gym, e) -> {
            if (e == null) {
                thisGym = gym;
                Objects.requireNonNull(getSupportActionBar()).setTitle(thisGym.getName());
            } else {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    private void queryEvent(String eventId) {
        ParseQuery<Event> eventParseQuery = ParseQuery.getQuery("Event");
        eventParseQuery.getInBackground(eventId, (event, e) -> {
            if (e == null) {
                thisEvent = event;
                setEventData();
                Objects.requireNonNull(getSupportActionBar()).setTitle(thisEvent.getGym().getName());
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
//TODO make sure this new one works correctly


//    private void skillLevel() {
//        //create a list of items for the spinner.
//        String[] items = getResources().getStringArray(R.array.skill_choices);
//        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
//        //There are multiple variations of this, but this is the basic variant.
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        //set the spinners adapter to the previously created one.
//        binding.spSkillLevel.setAdapter(adapter);
//        binding.spSkillLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                skillLevel = parent.getItemAtPosition(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }
}



