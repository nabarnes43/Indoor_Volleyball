package com.example.indoor_volleyball.Fragments.GymFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.indoor_volleyball.Activities.CreateEventActivity;
import com.example.indoor_volleyball.Adapters.GymAdapter;
import com.example.indoor_volleyball.MapsActivity;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.databinding.FragmentGymListBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;


public abstract class GymListFragment extends Fragment {
    FragmentGymListBinding binding;
    List<Gym> gymsFollowed;
    GymAdapter adapterUserGyms;
    RecyclerView rvGyms;
    int position;

    ActivityResultLauncher<Void> MapOpener = registerForActivityResult(new OpenMap(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean success) {
                    //TODO refresh list.
                }
            });

    public GymListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGymListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvGyms = binding.rvGyms;
        gymsFollowed = new ArrayList<>();
        adapterUserGyms = new GymAdapter(getContext(), gymsFollowed);
        rvGyms.setAdapter(adapterUserGyms);
        rvGyms.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchUserGymsAsync();

        binding.fabCreateGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapOpener.launch(null);
            }
        });
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

    private void fetchUserGymsAsync() {
        adapterUserGyms.clear();
        queryUsersGymsByDistance(ParseUser.getCurrentUser());
    }

    //Get a list of gyms that the user follows.
    private void queryUsersGymsByDistance(ParseUser user) {
        ParseQuery<Gym> query = getGymQuery(user);
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymList, ParseException e) {
                if (e == null) {
                    gymsFollowed.addAll(gymList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
                adapterUserGyms.notifyDataSetChanged();
            }
        });
    }

    public class OpenMap extends ActivityResultContract<Void, Boolean> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent i = new Intent(getContext(), MapsActivity.class);
            i.putExtra("gymList", Parcels.wrap(gymsFollowed));
            i.putExtra("fragmentPosition", Parcels.wrap(position));
            return i;
        }

        @Override
        public Boolean parseResult(int resultCode, @Nullable Intent result) {
            return resultCode == Activity.RESULT_OK;
        }
    }

}
