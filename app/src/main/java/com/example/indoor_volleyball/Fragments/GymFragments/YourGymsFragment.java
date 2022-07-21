package com.example.indoor_volleyball.Fragments.GymFragments;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.indoor_volleyball.Models.Gym;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@RequiresApi(api = Build.VERSION_CODES.N)
public class YourGymsFragment extends GymListFragment {

    public YourGymsFragment() {
        // Required empty public constructor
    }

    public YourGymsFragment(int position) {
        this.position = position;
    }

    @Override
    protected ParseQuery<Gym> getGymQuery(ParseUser user) {
        ParseQuery<Gym> query = super.getGymQuery(user);
        query.whereEqualTo("usersFollowing", user);
        return query;
    }
}
