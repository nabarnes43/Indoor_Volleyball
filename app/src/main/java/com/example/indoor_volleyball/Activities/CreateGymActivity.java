package com.example.indoor_volleyball.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Matrix;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.indoor_volleyball.Activities.Details.GymDetailActivity;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityCreateGymBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class CreateGymActivity extends AppCompatActivity {
    ActivityCreateGymBinding binding;
    private static final String TAG = "CreateGymActivity";
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGymBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key));
        binding.etGymAddress.setFocusable(false);
        binding.etGymAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ID, Place.Field.PHONE_NUMBER, Place.Field.OPENING_HOURS, Place.Field.RATING, Place.Field.WEBSITE_URI, Place.Field.BUSINESS_STATUS, Place.Field.UTC_OFFSET, Place.Field.PHOTO_METADATAS);
                //Create Intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).setTypeFilter(TypeFilter.ESTABLISHMENT).build(CreateGymActivity.this);
                //Start activity result I could not find the non depreciated.
                startActivityForResult(intent, 100);
            }
        });
    }
    //TODO if they try to create the same gym take them to the create event of that gym page.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //When success Initialize Place
            assert data != null;
            Place place = Autocomplete.getPlaceFromIntent(data);
            getGymPhoto(place);
            binding.etGymAddress.setText(place.getAddress());
            setTextOrHide(place.getName(), binding.tvName, R.string.name_status);
            binding.tvLocation.setText(String.valueOf(place.getLatLng()));
            setTextOrHide(place.getId(), binding.tvPlaceId, R.string.place_id_status);
            if (place.getOpeningHours() != null) {
                binding.tvOpeningHours.setVisibility(View.VISIBLE);
                setTextOrHide(place.getOpeningHours().getWeekdayText(), binding.tvOpeningHours, R.string.opening_hours_status);
            } else {
                binding.tvOpeningHours.setVisibility(View.GONE);
            }
            setTextOrHide(place.getWebsiteUri(), binding.tvWebsiteUrI, R.string.website_status);
            setTextOrHide(place.getBusinessStatus(), binding.tvBusinessStatus, R.string.business_status);
            setTextOrHide(place.getPhoneNumber(), binding.tvPhoneNumber, R.string.phone_number_status);
            setTextOrHide(place.getRating(), binding.tvRating, R.string.rating_status);
            setTextOrHide(place.isOpen(), binding.tvIsOpen, R.string.is_open_status);
            binding.btCreateGym.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        createGym(place);
                    } catch (ParseException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(CreateGymActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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

    private void getGymPhoto(Place place) {
        PlacesClient placesClient = Places.createClient(this);
        if (!(place.getPhotoMetadatas() == null)) {
            PhotoMetadata metaDate = place.getPhotoMetadatas().get(0);
            // Get the attribution text.
            final String attributions = metaDate.getAttributions();
            //Create a FetchPhotoRequest.
            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(metaDate)
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                bitmap = fetchPhotoResponse.getBitmap();
                Glide.with(CreateGymActivity.this).load(bitmap).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.ivGymPhotoSearch);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Toast.makeText(this, getString(R.string.place_not_found), Toast.LENGTH_SHORT).show();
                    final int statusCode = apiException.getStatusCode();
                    // TODO: Handle error with given status code.
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.location_has_no_image), Toast.LENGTH_SHORT).show();
        }
    }

    private void createGym(Place place) throws ParseException, IOException {
        Gym gym = new Gym();
        gym.setName(place.getName());
        //Converting from ArrayList to Json Array.
        if (!(place.getOpeningHours() == null)) {
            JSONArray openingHours = new JSONArray((place.getOpeningHours()).getWeekdayText());
            gym.setOpeningHours(openingHours);
        }
        gym.setAddress(place.getAddress());
        gym.setPlaceId(place.getId());
        //Converting LatLng to Geo Point.
        ParseGeoPoint gp = new ParseGeoPoint(((Objects.requireNonNull(place.getLatLng())).latitude), (place.getLatLng().longitude));
        gym.setLocation(gp);
        gym.setPhoneNumber(place.getPhoneNumber());
        gym.setRating(place.getRating());
        gym.setWebsiteUrl(Objects.requireNonNull(place.getWebsiteUri()).toString());
        //Converts Bitmap to file.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] bitmapBytes = stream.toByteArray();
        gym.setImage(new ParseFile("image", bitmapBytes));
        gym.save();
        Log.i(TAG, getString(R.string.save_succeeded_text));
        Toast.makeText(CreateGymActivity.this, getString(R.string.save_succeeded_text), Toast.LENGTH_SHORT).show();
        goToCreateEvent(gym);
    }

    private void goToCreateEvent(Gym gym) {
        String gymId = gym.getObjectId();
        Intent i = CreateEventActivity.newIntent(this, gymId);
        startActivity(i);
    }
}
