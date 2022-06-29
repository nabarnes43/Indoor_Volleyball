package com.example.indoor_volleyball.Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.indoor_volleyball.CreateEventActivity;
import com.example.indoor_volleyball.MainActivity;
import com.example.indoor_volleyball.QueryActivity;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivityMainBinding;
import com.example.indoor_volleyball.databinding.FragmentEventsBinding;


public class EventsFragment extends Fragment {
    private FragmentEventsBinding binding;
    ActivityResultLauncher<Void> EventCreator = registerForActivityResult(new CreateEvent(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean success) {
                    //TODO refresh list.
                }
            });

    public EventsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Set up all the views

        binding.btCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreateEventActivity.class);
                EventCreator.launch(null);
            }
        });

    }


    public class CreateEvent extends ActivityResultContract<Void,Boolean> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent i = new Intent(context, CreateEventActivity.class);
            return i;
        }

        @Override
        public Boolean parseResult(int resultCode, @Nullable Intent result) {
            return resultCode==Activity.RESULT_OK;
        }
    }



}