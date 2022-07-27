package com.example.indoor_volleyball.Adapters;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.indoor_volleyball.Fragments.GymFragments.AllGymsFragment;
import com.example.indoor_volleyball.Fragments.GymFragments.YourGymsFragment;
import com.example.indoor_volleyball.R;

public class GymsPagerAdapter extends FragmentStateAdapter {


    public GymsPagerAdapter(@NonNull Fragment gymFinderFragment) {
        super(gymFinderFragment);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
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
    public String getTitle(Context context, int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.nearby_gyms);
            case 1:
                return context.getString(R.string.your_gyms);
            default:
                throw new IllegalArgumentException(context.getString(R.string.method_only_supports_pager));
        }
    }
}
