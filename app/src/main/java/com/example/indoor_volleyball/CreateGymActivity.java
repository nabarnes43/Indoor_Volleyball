package com.example.indoor_volleyball;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.databinding.ActivityCreateGymBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


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
        Places.initialize(getApplicationContext(), "AIzaSyAHKBaHxum5kdPy9qXg2vSbj57te127ScA");

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
            Place place = Autocomplete.getPlaceFromIntent(data);

            binding.etGymAddress.setText(place.getAddress());

            binding.tvName.setText(String.format("Locality name: " + place.getName()));

            binding.tvLocation.setText(String.valueOf(place.getLatLng()));
            //TODO need code for if any of this information doesn't exist!
            binding.tvOpeningHours.setText("Opening Hours " + place.getOpeningHours().getWeekdayText());

            binding.tvBusinessStatus.setText("Business Status " + place.getBusinessStatus());

            binding.tvPhoneNumber.setText("Phone Number " + place.getPhoneNumber());

            binding.tvRating.setText("Rating " + (place.getRating()));

            binding.tvWebsiteUrI.setText("Website " + place.getWebsiteUri());

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

    private void createGym(Place place) throws ParseException {
        Gym gym = new Gym();
        gym.setName(place.getName());
        //Converting from ArrayList to Json Array.
        JSONArray openingHours = new JSONArray(place.getOpeningHours().getWeekdayText());
        gym.setOpeningHours(openingHours);
        gym.setAddress(place.getAddress());
        gym.setPlaceId(place.getId());
        //Converting LatLng to Geo Point.
        ParseGeoPoint gp = new ParseGeoPoint((place.getLatLng().latitude), (place.getLatLng().longitude));
        gym.setLocation(gp);
        gym.setPhoneNumber(place.getPhoneNumber());
        gym.setRating(place.getRating());
        gym.setWebsiteUrl(place.getWebsiteUri().toString());

        gym.save();

//        gym.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Error while Saving", e);
//                    Toast.makeText(CreateGymActivity.this, "Error saving post", Toast.LENGTH_SHORT);
//                }
//            }
//        });
        Log.i(TAG, "The save succeeded");
        Toast.makeText(CreateGymActivity.this, "The save succeeded", Toast.LENGTH_SHORT).show();
        goToCreateEvent(gym);
    }



    private void goToCreateEvent(Gym gym) {
        String gymId = gym.getObjectId();
        Intent i = new Intent(this, CreateEventActivity.class);
        i.putExtra("gymId", Parcels.wrap(gymId));
        startActivity(i);

    }



}
