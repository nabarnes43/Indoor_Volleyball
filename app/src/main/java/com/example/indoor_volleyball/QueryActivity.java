package com.example.indoor_volleyball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.databinding.ActivityQueryBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class QueryActivity extends AppCompatActivity {

    private static final String TAG = "QueryActivity";
    private ActivityQueryBinding binding;
    List<Gym> allGyms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        allGyms = new ArrayList<>();

        binding = ActivityQueryBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

        binding.tvQuery.setText("");
        queryAllGyms();

        binding.btAllGymsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i =0; i<allGyms.size();i++) {
                    String gymName = allGyms.get(i).getName();
                    int gymRating = allGyms.get(i).getRating();
                    ParseGeoPoint gymLocation = allGyms.get(i).getLocation();
                    String original_text = binding.tvAllGyms.getText().toString();
                    original_text = original_text + "Name: "+gymName + "\n Rating:" + gymRating+"\n Location :"+ gymLocation +"\n\n";
                    binding.tvAllGyms.setText(original_text);
                }
            }
        });


    }


    //Gets a list of all the gyms in the Database.
    private void queryAllGyms() {
        ParseQuery<Gym> query = ParseQuery.getQuery(Gym.class);

        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Gym>() {
            @Override
            public void done(List<Gym> gymList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    for (int i =0; i<gymList.size();i++) {
                        String gymName = gymList.get(i).getName();
                        int gymRating = gymList.get(i).getRating();
                        ParseGeoPoint gymLocation = gymList.get(i).getLocation();
                        Toast.makeText(QueryActivity.this, "Name: "+gymName + "\n Rating:" + gymRating+" Location :"+ gymLocation, Toast.LENGTH_LONG).show();
                    }
                    allGyms.addAll(gymList);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }


            }
        });

    }






}