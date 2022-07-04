package com.example.indoor_volleyball;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.indoor_volleyball.databinding.ActivityCreateEventBinding;
import com.example.indoor_volleyball.databinding.ActivityCreateGymBinding;



public class CreateGymActivity extends AppCompatActivity {
    ActivityCreateGymBinding binding;
    public static final String TAG ="CreateGymActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateGymBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

    }
}
