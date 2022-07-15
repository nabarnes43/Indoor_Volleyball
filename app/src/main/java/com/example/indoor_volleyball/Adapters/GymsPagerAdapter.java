package com.example.indoor_volleyball.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.indoor_volleyball.Fragments.GymFragments.AllGymsFragment;
import com.example.indoor_volleyball.Fragments.GymFragments.YourGymsFragment;

public class GymsPagerAdapter extends FragmentStateAdapter {


    public GymsPagerAdapter(@NonNull Fragment gymFinderFragment) {
        super(gymFinderFragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AllGymsFragment(position);
            case 1:
                return new YourGymsFragment(position);
            default:
                throw new IllegalArgumentException("Method only supports 0 and 1");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    //TODO use string resources before I commit.
    public String getTitle(int position) {
        switch (position) {
            case 0:
                return "All Gyms";
            case 1:
                return "Your Gyms";
            default:
                throw new IllegalArgumentException("Method only supports 0 and 1");
        }
    }
}
