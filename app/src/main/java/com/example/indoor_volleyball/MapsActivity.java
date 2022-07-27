package com.example.indoor_volleyball;//package com.example.indoor_volleyball;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.indoor_volleyball.Fragments.GymDetailDialogFragment;
import com.example.indoor_volleyball.Models.Gym;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
    private List<Gym> gymList;
    private int position;
    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private ParseGeoPoint currentUserLocation;

    //TODO bottom sheet dialog fragment.

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
            getSupportActionBar().setTitle(getString(R.string.nearby_gyms));
        } else {
            getSupportActionBar().setTitle(getString(R.string.your_gyms));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showCurrentUserInMap(mMap, find_Location(MapsActivity.this));
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

    //TODO add recenter button to action bar
    public void refreshQuery(GoogleMap googleMap, ParseUser user) {
        find_Location(MapsActivity.this);
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
                color = BitmapDescriptorFactory.HUE_ROSE;
            } else {
                color = BitmapDescriptorFactory.HUE_MAGENTA;
            }
            gymMarkerColor(gymLocation, color, gym);
        }
    }

    public void gymMarkerColor(LatLng latLng, Float color, Gym gym) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(gym.getName()).icon(BitmapDescriptorFactory.defaultMarker(color)));
        marker.setTag(gym);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                find_Location(MapsActivity.this);
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
            case R.id.btUserLocation:
                showCurrentUserInMap(mMap, find_Location(MapsActivity.this));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //TODO get geo point from zip code for fall back make the places call when user makes or edits their zipcode.
    @SuppressLint("MissingPermission")
    public Location find_Location(Context con) {
        Log.d("Find Location", "in find_location");
        //this.con = con;
        String location_context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) con.getSystemService(location_context);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, 1000, 0,
                    new LocationListener() {

                        public void onLocationChanged(Location location) {
                        }

                        public void onProviderDisabled(String provider) {
                        }

                        public void onProviderEnabled(String provider) {
                        }

                        public void onStatusChanged(String provider, int status,
                                                    Bundle extras) {
                        }
                    });
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                Toast.makeText(this, "User current location: " + location, Toast.LENGTH_SHORT).show();
                return location;
            }
        }
        return null;
    }

    private void getCurrentUserLocation(Location location) {
        currentUserLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
    }

    private void showCurrentUserInMap(final GoogleMap googleMap, Location location) {
        // creating a marker in the map showing the current user location
        LatLng currentUser = new LatLng(location.getLatitude(), location.getLongitude());
        getCurrentUserLocation(location);
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
        query.whereNear("location", currentUserLocation);
        query.setLimit(1);
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymList, ParseException e) {
                if (e == null) {
                    Gym gym = gymList.get(0);
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


