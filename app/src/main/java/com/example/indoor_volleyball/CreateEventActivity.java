package com.example.indoor_volleyball;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.databinding.ActivityCreateEventBinding;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {
    public static final String TAG = "CreateEventActivity";
    //TODO make everything private.
    Date startTime;
    Date endTime;
    Gym thisGym;
    String skillLevel;
    Boolean allowPlusOnes;
    Boolean allowSpectators;
    Calendar date;
    //TODO if it is user visible put it in the strings resource file.
    private SimpleDateFormat dateFormat = new SimpleDateFormat("M-dd-yyyy hh:mm:ss a");
    Boolean startTimeTrue;
    List<Gym> allGyms;
    ActivityCreateEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allGyms = new ArrayList<>();
        thisGym = getIntent().getParcelableExtra("Gym");
        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);


        skillLevel();

        allowPlusOnes();
        allowSpectators();

        try {
            queryAllGyms();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimeTrue = true;
                showDateTimePicker();
            }
        });

        binding.tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimeTrue = false;
                showDateTimePicker();
            }
        });


        binding.btCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minPlayers = Integer.parseInt(binding.etMinPlayers.getText().toString());
                int maxPlayers = Integer.parseInt(binding.etMaxPlayers.getText().toString());
                String details = binding.etDetails.getText().toString();
                String teamRotation = binding.etTeamRotation.getText().toString();
                String eventCode = binding.etEventCode.getText().toString();

                // Create the Event
                Event event = new Event();
                //properties
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
                event.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while Saving", e);
                            Toast.makeText(CreateEventActivity.this, "Error saving post", Toast.LENGTH_SHORT);
                        }
                    }
                });
                Log.i(TAG, "The save succeeded");
                Toast.makeText(CreateEventActivity.this, "The save succeeded", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //Gets a list of all the gyms in the Database gets the info gym maps page.
    private void queryAllGyms() throws ParseException {
        ParseQuery<Gym> query = ParseQuery.getQuery(Gym.class);
        allGyms.addAll(query.find());
    }


    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        Date thisDate;
        new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The choosen one " + date.getTime());
                        Toast.makeText(CreateEventActivity.this, "The choosen one " + date.getTime(), Toast.LENGTH_SHORT).show();
                        if (startTimeTrue) {
                            startTime = date.getTime();
                            Toast.makeText(CreateEventActivity.this, "Start Time" + date, Toast.LENGTH_SHORT).show();
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
        String[] items = new String[]{"AA", "A", "BB", "B", "C"};
        final String[] selected = {""};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        binding.spSkillLevel.setAdapter(adapter);
        binding.spSkillLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skillLevel = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void allowPlusOnes() {
        //create a list of items for the spinner.
        Boolean[] items = new Boolean[]{true, false};
        final Boolean[] selected = {true};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<Boolean> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        binding.spAllowPlusOnes.setAdapter(adapter);
        binding.spAllowPlusOnes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                allowPlusOnes = (Boolean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void allowSpectators() {
        //create a list of items for the spinner.
        Boolean[] items = new Boolean[]{true, false};
        final Boolean[] selected = {true};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<Boolean> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        binding.spAllowSpectators.setAdapter(adapter);
        binding.spAllowSpectators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                allowSpectators = (Boolean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}