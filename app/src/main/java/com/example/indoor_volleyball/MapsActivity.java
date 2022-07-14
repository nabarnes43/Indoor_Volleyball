package com.example.indoor_volleyball;//package com.example.indoor_volleyball;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.indoor_volleyball.Models.Gym;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

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
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
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
        mMap.setMinZoomPreference(3.0f);

        //showCurrentUserInMap(mMap);
        //TODO get rid of this when refresher is added
        gymList.clear();
        showClosestGym(mMap);
        if (!gymList.isEmpty()) {
            gymListToMarker(mMap);
        } else {
            if (position == 0) {
                queryGyms(mMap, getGymQuery(ParseUser.getCurrentUser()));
            } else {
                queryGyms(mMap, getUserGymQuery(ParseUser.getCurrentUser()));
            }
        }
    }

    public void refreshQuery(GoogleMap googleMap, ParseUser user) {
        if (position == 0) {
            queryGyms(googleMap, getGymQuery(user));
        } else {
            queryGyms(googleMap, getUserGymQuery(user));
        }
    }





    public void gymListToMarker(GoogleMap mMap) {
        for (Gym gym:gymList) {
            LatLng gymLocation = new LatLng(gym.getParseGeoPoint("location").getLatitude(), gym.getParseGeoPoint("location").getLongitude());
            if (position == 0) {
                mMap.addMarker(new MarkerOptions().position(gymLocation).title(gym.getString("name")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else {
                mMap.addMarker(new MarkerOptions().position(gymLocation).title(gym.getString("name")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
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
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else {
            // getting last know user's location
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            // checking if the location is null
            if(location != null){
                // if it isn't, save it to Back4App Dashboard
                currentUserLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                ParseUser currentUser = ParseUser.getCurrentUser();

                if (currentUser != null) {
                    currentUser.put("longLat", currentUserLocation);
                    currentUser.saveInBackground();
                } else {
                    // do something like coming back to the login activity
                }
            }
            else {
                // if it is null, do something like displaying error and coming back to the menu activity
            }
        }
    }

    /* saving the current user location, using the saveCurrentUserLocation method of Step 3, to avoid
 null pointer exception and also to return the user's most current location */
    private ParseGeoPoint getCurrentUserLocation(){

        // finding currentUser
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            // if it's not possible to find the user, do something like returning to login activity
        }
        // otherwise, return the current user location
        return currentUser.getParseGeoPoint("longLat");

    }

    private void showCurrentUserInMap(final GoogleMap googleMap){
        // calling retrieve user's location method of Step 4
        ParseGeoPoint currentUserLocation = getCurrentUserLocation();
        // creating a marker in the map showing the current user location
        LatLng currentUser = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(currentUser).title(ParseUser.getCurrentUser().getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        // zoom the map to the currentUserLocation
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser, 5));
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

    //Get a list of gyms that the user follows.
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

    private void showStoresInMap(final GoogleMap googleMap){
        ParseQuery<Gym> query = ParseQuery.getQuery("Gym");
        query.whereExists("location");
        query.findInBackground(new FindCallback<Gym>() {
            @Override  public void done(List<Gym> gymListH, ParseException e) {
                if (e == null) {
                    gymList.addAll(gymListH);
                    gymListToMarker(googleMap);
                } else {
                    // handle the error
                    Log.d("store", "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }

    private void showClosestGym(final GoogleMap googleMap){
        ParseQuery<Gym> query = ParseQuery.getQuery("Gym");
        query.whereNear("location", getCurrentUserLocation());
        // setting the limit of near stores to 1, you'll have in the nearStores list only one object: the closest store from the current user
        query.setLimit(1);
        query.findInBackground(new FindCallback<Gym>() {
            @Override  public void done(List<Gym> gymEvent, ParseException e) {
                if (e == null) {
                    Gym gym = gymEvent.get(0);
                    // showing current user location, using the method implemented in Step 5
                    //showCurrentUserInMap(mMap);
                    // finding and displaying the distance between the current user and the closest store to him, using method implemented in Step 4
                    //double distance = getCurrentUserLocation().distanceInMilesTo(gym.getParseGeoPoint("location"));
                    // creating a marker in the map showing the closest store to the current user
                    LatLng closestStoreLocation = new LatLng(gym.getParseGeoPoint("location").getLatitude(), gym.getParseGeoPoint("location").getLongitude());
                    // zoom the map to the closestStoreLocation
                    //Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
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


