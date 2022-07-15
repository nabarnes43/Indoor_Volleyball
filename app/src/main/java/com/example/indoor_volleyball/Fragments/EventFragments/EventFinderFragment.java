package com.example.indoor_volleyball.Fragments.EventFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indoor_volleyball.Adapters.EventsPagerAdapter;
import com.example.indoor_volleyball.Adapters.GymsPagerAdapter;
import com.example.indoor_volleyball.databinding.FragmentEventFinderBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class EventFinderFragment extends Fragment {
    private FragmentEventFinderBinding binding;

    public EventFinderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventFinderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventsPagerAdapter eventsPagerAdapter = new EventsPagerAdapter(this);
        binding.vpEventList.setAdapter(eventsPagerAdapter);
        TabLayout tlGymsList = binding.tlEventList;
        new TabLayoutMediator(tlGymsList, binding.vpEventList,
                (tab, position) -> tab.setText(eventsPagerAdapter.getTitle(position))).attach();
    }
}
