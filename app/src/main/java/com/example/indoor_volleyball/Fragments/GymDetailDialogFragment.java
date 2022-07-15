package com.example.indoor_volleyball.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indoor_volleyball.Activities.Details.GymDetailActivity;
import com.example.indoor_volleyball.Fragments.GymFragments.GymListFragment;
import com.example.indoor_volleyball.MapsActivity;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.FragmentGymDetailDialogFraagmentBinding;
import com.example.indoor_volleyball.databinding.FragmentGymFinderBinding;
import com.parse.ParseException;

import org.parceler.Parcels;

public class GymDetailDialogFragment extends DialogFragment {
    FragmentGymDetailDialogFraagmentBinding binding;
    Gym gym;
    ActivityResultLauncher<Void> GymDetailOpener = registerForActivityResult(new GymDetailedActivity(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean success) {
                    //TODO refresh list.
                }
            });


    public GymDetailDialogFragment() {
    }

    public static GymDetailDialogFragment newInstance(Gym gym) {
        GymDetailDialogFragment frag = new GymDetailDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("gym", gym);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGymDetailDialogFraagmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gym = getArguments().getParcelable("gym");
        binding.itmEventItem.tvGymName.setText(gym.getName());
        binding.itmEventItem.rbGymRating.setRating(gym.getRating().floatValue());
        try {
            binding.itmEventItem.tvEventDateDescription.setText(gym.getNextEvent().getDetails().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getDialog().getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        binding.itmEventItem.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GymDetailOpener.launch(null);
            }
        });
    }

    public class GymDetailedActivity extends ActivityResultContract<Void, Boolean> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent i = new Intent(getContext(), GymDetailActivity.class);
            i.putExtra("gym", Parcels.wrap(gym));
            return i;
        }

        @Override
        public Boolean parseResult(int resultCode, @Nullable Intent result) {
            return resultCode == Activity.RESULT_OK;
        }
    }
}