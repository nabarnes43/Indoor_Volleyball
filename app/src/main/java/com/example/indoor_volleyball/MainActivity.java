package com.example.indoor_volleyball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.indoor_volleyball.databinding.ActivityLoginBinding;
import com.example.indoor_volleyball.databinding.ActivityMainBinding;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private Button btLogOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);


        binding.btLogOut.setOnClickListener(v -> ParseUser.logOutInBackground(e -> {

            if(e != null) {
                Log.e(TAG, "Error signing out", e);
                Toast.makeText(MainActivity.this, "Error signing out", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.i(TAG, "Sign out successful");
            goToLoginActivity();
            Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
        }));

    }

    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}