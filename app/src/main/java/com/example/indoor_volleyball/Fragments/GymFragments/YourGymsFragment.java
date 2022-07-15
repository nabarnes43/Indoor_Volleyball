package com.example.indoor_volleyball.Fragments.GymFragments;

import com.example.indoor_volleyball.Models.Gym;
import com.parse.ParseQuery;
import com.parse.ParseUser;


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
        //TODO should these be string resource files.
        query.whereEqualTo("usersFollowing", user);
        return query;
    }

}
