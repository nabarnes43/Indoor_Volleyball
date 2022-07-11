package com.example.indoor_volleyball.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.indoor_volleyball.Fragments.AllGymsFragment;
import com.example.indoor_volleyball.Fragments.MapsFragment;
import com.example.indoor_volleyball.Fragments.YourGymsFragment;

public class PagerAdapter extends FragmentStateAdapter {

    private int numOfTabs;

    public PagerAdapter(@NonNull Fragment gymFinderFragment) {
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
