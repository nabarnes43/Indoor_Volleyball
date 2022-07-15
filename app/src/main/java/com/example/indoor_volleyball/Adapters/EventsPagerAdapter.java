package com.example.indoor_volleyball.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.indoor_volleyball.Fragments.EventFragments.EventsAttendingFragment;
import com.example.indoor_volleyball.Fragments.EventFragments.EventsCreatedFragment;

public class EventsPagerAdapter extends FragmentStateAdapter {


    public EventsPagerAdapter(@NonNull Fragment eventFinderFragment) {
        super(eventFinderFragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new EventsAttendingFragment();
            case 1:
                return new EventsCreatedFragment();
            default:
                throw new IllegalArgumentException("Method only supports 0, 1 and 2");
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
                return "Events Attending";
            case 1:
                return "Events Created";
            default:
                throw new IllegalArgumentException("Method only supports 0 and 1");
        }
    }


}
