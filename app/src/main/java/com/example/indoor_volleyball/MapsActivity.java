package com.example.indoor_volleyball;//package com.example.indoor_volleyball;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.example.indoor_volleyball.Fragments.GymDetailDialogFragment;
import com.example.indoor_volleyball.Models.Gym;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    List<Gym> gymList;
    private int position;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    ParseGeoPoint currentUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gymList = new ArrayList<>();
        gymList = Parcels.unwrap(getIntent().getParcelableExtra("gymList"));
        position = Parcels.unwrap(getIntent().getParcelableExtra("fragmentPosition"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        if (position == 0) {
            getSupportActionBar().setTitle("All Gyms");
        } else {
            getSupportActionBar().setTitle("Your Gyms");
        }
    }

    //TODO each fragment a floating action button. Add coordinator layout.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        saveCurrentUserLocation();
        showCurrentUserInMap(mMap);
        if (!gymList.isEmpty()) {
            gymListToMarker(mMap);
        } else {
            if (position == 0) {
                queryGyms(mMap, getGymQuery(ParseUser.getCurrentUser()));
            } else {
                queryGyms(mMap, getUserGymQuery(ParseUser.getCurrentUser()));
            }
        }
        mMap.setOnMarkerClickListener(this);
    }

    public boolean onMarkerClick(final Marker marker) {
        if (marker.getTag() instanceof Gym) {
            Gym gym = (Gym) marker.getTag();
            showEditDialog(gym);
        }
        return false;
    }

    private void showEditDialog(Gym gym) {
        FragmentManager fm = getSupportFragmentManager();
        GymDetailDialogFragment gymDetailDialogFragment = GymDetailDialogFragment.newInstance(gym);
        gymDetailDialogFragment.show(fm, "fragment_gym_detail_dialog_fragment");
    }

    public void refreshQuery(GoogleMap googleMap, ParseUser user) {
        saveCurrentUserLocation();
        ParseQuery<Gym> gymQuery;
        if (position == 0) {
            gymQuery = getGymQuery(user);
        } else {
            gymQuery = getUserGymQuery(user);
        }
        queryGyms(googleMap, gymQuery);
    }

    public void gymListToMarker(GoogleMap mMap) {
        for (Gym gym : gymList) {
            LatLng gymLocation = new LatLng(gym.getParseGeoPoint("location").getLatitude(), gym.getParseGeoPoint("location").getLongitude());
            Float color;
            if (position == 0) {
                color = BitmapDescriptorFactory.HUE_AZURE;
            } else {
                color = BitmapDescriptorFactory.HUE_GREEN;
            }
            gymMarkerColor(gymLocation, color, gym);
        }
    }

    public Marker gymMarkerColor(LatLng latLng, Float color, Gym gym) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(gym.getName()).icon(BitmapDescriptorFactory.defaultMarker(color)));
        marker.setTag(gym);
        return marker;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                saveCurrentUserLocation();
                break;
        }
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.btRefresh:
                refreshQuery(mMap, ParseUser.getCurrentUser());
                Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveCurrentUserLocation() {
        // requesting permission to get user's location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            // getting last know user's location
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            // checking if the location is null
            if (location != null) {
                // if it isn't, save it to Back4App Dashboard
                currentUserLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.put("longLat", currentUserLocation);
                currentUser.saveInBackground();
                showClosestGym(mMap);
                Toast.makeText(this, "User current location: " + currentUserLocation, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error with location check settings!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ParseGeoPoint getCurrentUserLocation() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser.getParseGeoPoint("longLat");
    }

    private void showCurrentUserInMap(final GoogleMap googleMap) {
        // calling retrieve user's location method of Step 4
        ParseGeoPoint currentUserLocation = getCurrentUserLocation();
        // creating a marker in the map showing the current user location
        LatLng currentUser = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(currentUser).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        // zoom the map to the currentUserLocation
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser, 11));
    }

    protected ParseQuery<Gym> getGymQuery(ParseUser user) {
        ParseGeoPoint userLocation = user.getParseGeoPoint("longLat");
        ParseQuery<Gym> query = new ParseQuery<>("Gym");
        query.whereNear("location", userLocation);
        query.include("details");
        query.include("startTime");
        query.include("endTime");
        query.include("nextEvent");
        return query;
    }

    protected ParseQuery<Gym> getUserGymQuery(ParseUser user) {
        ParseQuery<Gym> query = getGymQuery(user);
        query.whereEqualTo("usersFollowing", user);
        return query;
    }

    private void queryGyms(final GoogleMap googleMap, ParseQuery<Gym> gymQuery) {
        ParseQuery<Gym> query = gymQuery;
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymListH, ParseException e) {
                if (e == null) {
                    gymList.addAll(gymListH);
                    gymListToMarker(googleMap);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void showClosestGym(final GoogleMap googleMap) {
        ParseQuery<Gym> query = ParseQuery.getQuery("Gym");
        query.whereNear("location", getCurrentUserLocation());
        query.setLimit(1);
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymEvent, ParseException e) {
                if (e == null) {
                    Gym gym = gymEvent.get(0);
                    LatLng closestStoreLocation = new LatLng(gym.getParseGeoPoint("location").getLatitude(), gym.getParseGeoPoint("location").getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(closestStoreLocation)
                            .zoom(11)                   // Sets the zoom
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else {
                    Log.d("gym", "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }

}


