package com.example.indoor_volleyball.Fragments.GymFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.indoor_volleyball.Activities.CreateEventActivity;
import com.example.indoor_volleyball.Activities.CreateGymActivity;
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
    ActivityResultLauncher<Void> GymCreator = registerForActivityResult(new CreateGym(),
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
        binding.tvGymEmpty.setVisibility(View.INVISIBLE);
        rvGyms = binding.rvGyms;
        gymsFollowed = new ArrayList<>();
        adapterUserGyms = new GymAdapter(getContext(), gymsFollowed);
        rvGyms.setAdapter(adapterUserGyms);
        rvGyms.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchUserGymsAsync();
        rvGyms.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && binding.fabCreateGym.isShown() && binding.fabGoToMap.isShown()) {
                    binding.fabCreateGym.hide();
                    binding.fabGoToMap.hide();
                }
            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.canScrollVertically(1)) {
                    binding.fabGoToMap.show();
                    binding.fabCreateGym.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        binding.fabGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapOpener.launch(null);
            }
        });
        binding.fabCreateGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GymCreator.launch(null);
            }
        });
        // Setup refresh listener which triggers new data loading
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchUserGymsAsync();
                binding.swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
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
                    if (gymList.isEmpty()) {
                        binding.tvGymEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                    Toast.makeText(getContext(), "Error " + e, Toast.LENGTH_SHORT).show();
                }
                adapterUserGyms.notifyDataSetChanged();
            }
        });
    }

    public class CreateGym extends ActivityResultContract<Void, Boolean> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent i = new Intent(context, CreateGymActivity.class);
            return i;
        }
        @Override
        public Boolean parseResult(int resultCode, @Nullable Intent result) {
            return resultCode == Activity.RESULT_OK;
        }
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
