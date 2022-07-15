package com.example.indoor_volleyball.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.indoor_volleyball.databinding.ActivitySigninBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private ActivitySigninBinding binding;
    private EditText etSignUpUsername;
    private EditText etSignUpPassword;
    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    private EditText etAge;
    private EditText etCity;
    private EditText etZipCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // inflating our xml layout in our activity main binding
        binding = ActivitySigninBinding.inflate(getLayoutInflater());

        // getting our root layout in our view.
        View view = binding.getRoot();

        // below line is to set
        // Content view for our layout.
        setContentView(view);
        //TODO input management.

        // calling button and setting on click listener for our button.
        // we have called our button with its id and set on click listener on it.
        binding.btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etSignUpUsername.getText().toString();
                String password = binding.etSignUpPassword.getText().toString();
                String name = binding.etName.getText().toString();
                String email = binding.etEmail.getText().toString();
                String phone = binding.etPhone.getText().toString();
                String address = binding.etAddress.getText().toString();
                int age = Integer.parseInt(binding.etAge.getText().toString());
                String city = binding.etCity.getText().toString();
                String zipCode = binding.etZipCode.getText().toString();

                // Create the ParseUser
                ParseUser user = new ParseUser();
                //Set core properties
                user.setUsername(username);
                user.setPassword(password);
                //custom properties
                user.put("email", email);
                user.put("name", name);
                user.put("phoneNumber", phone);
                user.put("age", age);
                user.put("address", address);
                user.put("city", city);
                user.put("zipCode", zipCode);


                // Invoke signUpInBackground
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Hooray! Let them use the app now.
                            goLoginActivity();
                            Toast.makeText(SignUpActivity.this, "Account Created. Logging In.", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e(TAG, "Signup failed " + e);
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                        }
                    }
                });
            }
        });
    }


    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }


}