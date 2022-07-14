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

import java.util.List;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    CameraPosition cameraPosition;

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

        Toast.makeText(this, "Current user location: " + getCurrentUserLocation(), Toast.LENGTH_SHORT).show();


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //Give earch fragment a floating action button. Add coordinator layout.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(20.0f);



        //showCurrentUserInMap(mMap);

        showClosestStore(mMap);
        showStoresInMap(mMap);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle("Maps");
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

    private void showStoresInMap(final GoogleMap googleMap){

        ParseQuery<Gym> query = ParseQuery.getQuery("Gym");
        query.whereExists("location");
        query.findInBackground(new FindCallback<Gym>() {
            @Override  public void done(List<Gym> gymList, ParseException e) {
                if (e == null) {
                    for(int i = 0; i < gymList.size(); i++) {
                        LatLng gymLocation = new LatLng(gymList.get(i).getParseGeoPoint("location").getLatitude(), gymList.get(i).getParseGeoPoint("location").getLongitude());
                        Toast.makeText(MapsActivity.this, gymLocation.toString(), Toast.LENGTH_SHORT).show();
                        mMap.addMarker(new MarkerOptions().position(gymLocation).title(gymList.get(i).getString("name")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                    }
                } else {
                    // handle the error
                    Log.d("store", "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }

    private void showClosestStore(final GoogleMap googleMap){
        ParseQuery<Gym> query = ParseQuery.getQuery("Gym");
        query.whereNear("location", getCurrentUserLocation());
        // setting the limit of near stores to 1, you'll have in the nearStores list only one object: the closest store from the current user
        query.setLimit(1);
        query.findInBackground(new FindCallback<Gym>() {
            @Override  public void done(List<Gym> gymEvent, ParseException e) {
                if (e == null) {
                    Gym gym = gymEvent.get(0);
                    // showing current user location, using the method implemented in Step 5
                    showCurrentUserInMap(mMap);
                    // finding and displaying the distance between the current user and the closest store to him, using method implemented in Step 4
                    double distance = getCurrentUserLocation().distanceInKilometersTo(gym.getParseGeoPoint("location"));
                    alertDisplayer("We found the closest store from you!", "It's " + gym.getString("Name") + ". \nYou are " + Math.round (distance * 100.0) / 100.0  + " km from this store.");
                    // creating a marker in the map showing the closest store to the current user
                    LatLng closestStoreLocation = new LatLng(gym.getParseGeoPoint("location").getLatitude(), gym.getParseGeoPoint("location").getLongitude());
                    mMap.addMarker(new MarkerOptions().position(closestStoreLocation).title(gym.getString("name")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    // zoom the map to the closestStoreLocation

                    //Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(closestStoreLocation)
                            .zoom(3)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
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

    private void alertDisplayer(String title,String message){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog ok = builder.create();
        ok.show();
    }
}


