package com.example.indoor_volleyball.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.indoor_volleyball.Fragments.EventFragments.EventsAttendingFragment;
import com.example.indoor_volleyball.Fragments.EventFragments.EventsCreatedFragment;
import com.example.indoor_volleyball.R;

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
    public String getTitle(Context context, int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.events_attending);
            case 1:
                return context.getString(R.string.evnets_created);
            default:
                throw new IllegalArgumentException("Method only supports 0 and 1");
        }
    }


}
