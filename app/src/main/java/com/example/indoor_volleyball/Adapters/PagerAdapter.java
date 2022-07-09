package com.example.indoor_volleyball.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.indoor_volleyball.Fragments.AllGymsFragment;
import com.example.indoor_volleyball.Fragments.MapsFragment;
import com.example.indoor_volleyball.Fragments.YourGymsFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new AllGymsFragment();
            case 1:
                return new YourGymsFragment();
            case 2:
                return new MapsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
