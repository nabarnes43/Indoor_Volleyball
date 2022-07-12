package com.example.indoor_volleyball.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.indoor_volleyball.Fragments.GymFragments.AllGymsFragment;
import com.example.indoor_volleyball.Fragments.MapsFragment;
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
                return new AllGymsFragment();
            case 1:
                return new YourGymsFragment();
            case 2:
                return new MapsFragment();
            default:
                throw new IllegalArgumentException("Method only supports 0, 1 and 2");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    //TODO use string resources before I commit.
    public String getTitle(int position) {
        switch (position) {
            case 0:
                return "All Gyms";
            case 1:
                return "Your Gyms";
            case 2:
                return "Maps";
            default:
                throw new IllegalArgumentException("Method only supports 0, 1 and 2");
        }
    }
}
