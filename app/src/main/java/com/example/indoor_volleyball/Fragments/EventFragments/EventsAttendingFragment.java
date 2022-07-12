package com.example.indoor_volleyball.Fragments.EventFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.FragmentEventFinderBinding;
import com.example.indoor_volleyball.databinding.FragmentEventsAttendingBinding;


public class EventsAttendingFragment extends Fragment {
    private FragmentEventsAttendingBinding binding;

    public EventsAttendingFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventsAttendingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}