package com.example.indoor_volleyball.Fragments.GymFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indoor_volleyball.Activities.CreateGymActivity;
import com.example.indoor_volleyball.Adapters.GymAdapter;
import com.example.indoor_volleyball.Adapters.GymsPagerAdapter;
import com.example.indoor_volleyball.Fragments.ProfileFragment;
import com.example.indoor_volleyball.MapsActivity;
import com.example.indoor_volleyball.Models.Gym;


import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.indoor_volleyball.databinding.FragmentGymFinderBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;


public class GymFinderFragment extends Fragment {
    public static final String TAG = "GymFinderFragment";
    private FragmentGymFinderBinding binding;

    public GymFinderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGymFinderBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GymsPagerAdapter gymsPagerAdapter = new GymsPagerAdapter(this);
        binding.vpGymList.setAdapter(gymsPagerAdapter);
        TabLayout tlGymsList = binding.tlGymsList;
        new TabLayoutMediator(tlGymsList, binding.vpGymList,
                (tab, position) -> tab.setText(gymsPagerAdapter.getTitle(position))).attach();
    }
}

