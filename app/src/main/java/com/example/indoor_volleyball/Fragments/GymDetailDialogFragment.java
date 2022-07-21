package com.example.indoor_volleyball.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.indoor_volleyball.Activities.Details.GymDetailActivity;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.FragmentGymDetailDialogFraagmentBinding;
import com.parse.ParseException;
import com.parse.ParseFile;

public class GymDetailDialogFragment extends DialogFragment {
    private static final String TAG = "GymDetailDialogFragment";
    private FragmentGymDetailDialogFraagmentBinding binding;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGymDetailDialogFraagmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Gym gym;
        assert getArguments() != null;
        gym = getArguments().getParcelable("gym");
        binding.itmEventItem.tvGymName.setText(gym.getName());
        binding.itmEventItem.rbGymRating.setRating(gym.getRating().floatValue());
        ParseFile image = gym.getImage();
        if (image != null) {
            Glide.with(requireContext()).load(image.getUrl()).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.itmEventItem.ivGymPhoto);
        } else {
            Glide.with(requireContext()).load(R.drawable.icon_gym_black).transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(30))).into(binding.itmEventItem.ivGymPhoto);
        }
        try {
            if (gym.getNextEvent() != null) {
                binding.itmEventItem.tvEventDateDescription.setText(gym.getNextEvent().getDetails());
            } else {
                binding.itmEventItem.tvEventDateDescription.setText(getString(R.string.no_events_at_gym));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getDialog().getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        binding.itmEventItem.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gymId = gym.getObjectId();
                Intent i = GymDetailActivity.newIntent(v.getContext(), gymId);
                startActivity(i);
            }
        });
    }
}