package com.example.indoor_volleyball.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.indoor_volleyball.Activities.CreateEventActivity;
import com.example.indoor_volleyball.Activities.CreateGymActivity;
import com.example.indoor_volleyball.Activities.LoginActivity;
import com.example.indoor_volleyball.Activities.QueryActivity;
import com.example.indoor_volleyball.databinding.FragmentProfileBinding;
import com.parse.ParseUser;


public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    ActivityResultLauncher<Void> EventCreator = registerForActivityResult(new CreateEvent(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean success) {
                    //TODO refresh list.
                }
            });

    ActivityResultLauncher<Void> GymCreator = registerForActivityResult(new CreateGym(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean success) {
                    //TODO refresh list.
                }
            });

    ActivityResultLauncher<Void> Logout = registerForActivityResult(new Logout(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean success) {
                    //TODO refresh list.
                }
            });

    ActivityResultLauncher<Void> Query = registerForActivityResult(new Query(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean success) {
                    //TODO refresh list.
                }
            });

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreateEventActivity.class);
                EventCreator.launch(null);
            }
        });

        binding.btCreateGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreateGymActivity.class);
                GymCreator.launch(null);
            }
        });

        binding.btQueryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), QueryActivity.class);
                Query.launch(null);
            }
        });

        binding.btLogOut.setOnClickListener(v -> ParseUser.logOutInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Error signing out", e);
                Toast.makeText(getContext(), "Error signing out", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i(TAG, "Sign out successful");
            Intent i = new Intent(getContext(), LoginActivity.class);
            Logout.launch(null);
            Toast.makeText(getContext(), "Signed out", Toast.LENGTH_SHORT).show();
        }));
    }

    public class CreateEvent extends ActivityResultContract<Void, Boolean> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent i = new Intent(context, CreateEventActivity.class);
            return i;
        }

        @Override
        public Boolean parseResult(int resultCode, @Nullable Intent result) {
            return resultCode == Activity.RESULT_OK;
        }
    }

    public class CreateGym extends ActivityResultContract<Void, Boolean> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent i = new Intent(context, CreateGymActivity.class);
            return i;
        }

        @Override
        public Boolean parseResult(int resultCode, @Nullable Intent result) {
            return resultCode == Activity.RESULT_OK;
        }
    }

    public class Logout extends ActivityResultContract<Void, Boolean> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent i = new Intent(context, LoginActivity.class);
            return i;
        }

        @Override
        public Boolean parseResult(int resultCode, @Nullable Intent result) {
            return resultCode == Activity.RESULT_OK;
        }
    }

    public class Query extends ActivityResultContract<Void, Boolean> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent i = new Intent(context, QueryActivity.class);
            return i;
        }

        @Override
        public Boolean parseResult(int resultCode, @Nullable Intent result) {
            return resultCode == Activity.RESULT_OK;
        }
    }
}
