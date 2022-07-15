package com.example.indoor_volleyball.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityCreateGymBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


public class CreateGymActivity extends AppCompatActivity {
    ActivityCreateGymBinding binding;
    public static final String TAG = "CreateGymActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGymBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //Initialize Places
        //TODO hide in secrets file.

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key));


        //Set edit text nonFocusable
        binding.etGymAddress.setFocusable(false);
        binding.etGymAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ID, Place.Field.PHONE_NUMBER, Place.Field.OPENING_HOURS, Place.Field.RATING, Place.Field.WEBSITE_URI, Place.Field.BUSINESS_STATUS, Place.Field.UTC_OFFSET);

                //Create Intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(CreateGymActivity.this);

                //Start activity result
                startActivityForResult(intent, 100);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //When success
            //Initialize Place
            assert data != null;
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.etGymAddress.setText(place.getAddress());
            binding.tvName.setText("Locality name: " + place.getName());
            binding.tvLocation.setText(String.valueOf(place.getLatLng()));
            //TODO need code for if any of this information doesn't exist!
            //TODO make a function takes string view.
            if (place.getOpeningHours() != null) {
                binding.tvOpeningHours.setVisibility(View.VISIBLE);
                binding.tvOpeningHours.setText("Opening Hours " + place.getOpeningHours().getWeekdayText());
            } else {
                binding.tvOpeningHours.setVisibility(View.GONE);
            }

            if (place.getOpeningHours() != null) {
                binding.tvOpeningHours.setVisibility(View.VISIBLE);
                binding.tvWebsiteUrI.setText("Website " + place.getWebsiteUri());
            } else {
                binding.tvOpeningHours.setVisibility(View.GONE);
            }
            //TODO check link Andrew left on pull request.
            binding.tvBusinessStatus.setText("Business Status " + place.getBusinessStatus());
            setTextOrHide(place.getBusinessStatus(), binding.tvBusinessStatus, R.string.business_status);
            binding.tvPhoneNumber.setText("Phone Number " + place.getPhoneNumber());

            binding.tvRating.setText("Rating " + (place.getRating()));

            binding.tvPlaceId.setText("Place Id " + place.getId());

            binding.tvIsOpen.setText("Is Open? " + place.isOpen());

            binding.btCreateGym.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        createGym(place);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            //When error
            //Initialize Status
            Status status = Autocomplete.getStatusFromIntent(data);
            //Display toast
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setTextOrHide(Object text, TextView view, int formatId) {
        if (text != null) {
            view.setVisibility(View.VISIBLE);
            String formattedString = getString(formatId, text.toString());
            view.setText(formattedString);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void createGym(Place place) throws ParseException {
        Gym gym = new Gym();
        gym.setName(place.getName());
        //Converting from ArrayList to Json Array.
        JSONArray openingHours = new JSONArray(Objects.requireNonNull(place.getOpeningHours()).getWeekdayText());
        gym.setOpeningHours(openingHours);
        gym.setAddress(place.getAddress());
        gym.setPlaceId(place.getId());
        //Converting LatLng to Geo Point.
        ParseGeoPoint gp = new ParseGeoPoint((Objects.requireNonNull(place.getLatLng()).latitude), (place.getLatLng().longitude));
        gym.setLocation(gp);
        gym.setPhoneNumber(place.getPhoneNumber());
        gym.setRating(place.getRating());
        gym.setWebsiteUrl(Objects.requireNonNull(place.getWebsiteUri()).toString());
        gym.save();
        Log.i(TAG, getString(R.string.save_succeeded_text));
        Toast.makeText(CreateGymActivity.this, getString(R.string.save_succeeded_text), Toast.LENGTH_SHORT).show();
        goToCreateEvent(gym);
    }
    //TODO need a purge that runs after create a event for gyms with 0 events cause code is dependent on gyms having at least one event.
    private void goToCreateEvent(Gym gym) {
        String gymId = gym.getObjectId();
        Intent i = new Intent(this, CreateEventActivity.class);
        i.putExtra(getString(R.string.gym_id_parcel_tag), Parcels.wrap(gymId));
        startActivity(i);
    }


}
