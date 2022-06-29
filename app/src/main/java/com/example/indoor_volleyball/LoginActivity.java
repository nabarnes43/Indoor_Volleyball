package com.example.indoor_volleyball;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;



import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.indoor_volleyball.databinding.ActivityLoginBinding;
import com.example.indoor_volleyball.databinding.ActivityMainBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    //private EditText etUsername;
    //private EditText etPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser()!=null) {
            goMainActivity();
        }
        //TODO add cut out all my excess code relating to the bindings in both login and sign up.
        // inflating our xml layout in our activity main binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        // getting our root layout in our view.
        View view = binding.getRoot();

        // below line is to set
        // Content view for our layout.
        setContentView(view);

        //etPassword = binding.etPassword;
        //etUsername = binding.etUsername;

        // calling button and setting on click listener for our button.
        // we have called our button with its id and set on click listener on it.
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();

                if(username.isEmpty()||password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter something", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(username,password);
                }
            }
        });

        binding.btLoginSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUpActivity();
            }
        });
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                binding.etPassword.setText("");
                binding.etUsername.setText("");
                if (e!= null) {
                    //TODO better error handling
                    Toast.makeText(LoginActivity.this,"Login Failed.\n Try Again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this,"Success!", Toast.LENGTH_SHORT).show();
                goMainActivity() ;
            }
        });
    }

    private void goMainActivity() {
         Intent i = new Intent(this, MainActivity.class);
         startActivity(i);
         finish();
    }

    private void goToSignUpActivity() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }




}