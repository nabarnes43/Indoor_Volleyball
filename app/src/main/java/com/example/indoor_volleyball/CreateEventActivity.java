package com.example.indoor_volleyball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.indoor_volleyball.databinding.ActivityCreateEventBinding;
import com.example.indoor_volleyball.databinding.ActivityQueryBinding;

public class CreateEventActivity extends AppCompatActivity {
    ActivityCreateEventBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

        skillLevel();
        allowPlusOnes();
        allowSpectators();


    }


    private void skillLevel() {
        //create a list of items for the spinner.
        String[] items = new String[]{"AA", "A", "BB", "B", "C"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        binding.spSkillLevel.setAdapter(adapter);
        binding.spSkillLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreateEventActivity.this, (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void allowPlusOnes() {
        //create a list of items for the spinner.
        Boolean[] items = new Boolean[]{true, false};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<Boolean> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        binding.spAllowPlusOnes.setAdapter(adapter);
        binding.spAllowPlusOnes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreateEventActivity.this,  parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void allowSpectators() {
        //create a list of items for the spinner.
        Boolean[] items = new Boolean[]{true, false};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<Boolean> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        binding.spAllowSpectators.setAdapter(adapter);
        binding.spAllowSpectators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreateEventActivity.this,  parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


}