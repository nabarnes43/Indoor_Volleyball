package com.example.indoor_volleyball.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.example.indoor_volleyball.Activities.CreateEventActivity;
import com.example.indoor_volleyball.Activities.CreateGymActivity;
import com.example.indoor_volleyball.Activities.LoginActivity;
import com.example.indoor_volleyball.Activities.QueryActivity;
import com.example.indoor_volleyball.databinding.FragmentProfileBinding;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.util.Objects;


public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private ParseUser user;
    private FragmentProfileBinding binding;
    public File photoFile;
    public String photoFileName = "photo.jpg";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
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
        user = ParseUser.getCurrentUser();
        displayUserInfo();
        binding.btTakePhotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
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

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    public void displayUserInfo() {
        binding.tvUsername.setText(user.getUsername());
        ParseFile profilePhoto = user.getParseFile("profilePhoto");
        if (profilePhoto != null) {
            Glide.with(requireContext()).load(user.getParseFile("profilePhoto").getUrl()).circleCrop().into(binding.ivProfilePhoto);
        } else {
            Toast.makeText(getContext(), "this Profile photo does not exist for user " + user.getUsername(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                binding.ivProfilePhoto.setImageBitmap(takenImage);
                Glide.with(requireContext()).load(takenImage).circleCrop().into(binding.ivProfilePhoto);
                binding.ivProfilePhoto.setRotation(90);
                user.put("profilePhoto", new ParseFile(photoFile));
                user.saveInBackground();
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
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
            return resultCode == RESULT_OK;
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
            return resultCode == RESULT_OK;
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
            return resultCode == RESULT_OK;
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
            return resultCode == RESULT_OK;
        }
    }
}
