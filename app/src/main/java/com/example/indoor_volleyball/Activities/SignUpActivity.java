package com.example.indoor_volleyball.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.R;
import com.example.indoor_volleyball.databinding.ActivitySigninBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private ActivitySigninBinding binding;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // get the content of both the edit text
            String username = binding.etSignUpUsername.getText().toString();
            String password = binding.etSignUpPassword.getText().toString();
            String name = binding.etName.getText().toString();
            String email = binding.etEmail.getText().toString();
            String phone = binding.etPhone.getText().toString();
            String zipCode = binding.etZipCode.getText().toString();
            // check whether both the fields are empty or not
            binding.btSignUp.setEnabled(!username.isEmpty() && !password.isEmpty() && !name.isEmpty()
                    && !email.isEmpty() && !phone.isEmpty() && !zipCode.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupFloatingLabelErrorLength(binding.usernameTextInputLayout);
        setupFloatingLabelErrorLength(binding.phoneTextInputLayout);
        setupFloatingLabelErrorLength(binding.zipCodeTextInputLayout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.string.action_bar_primary)));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        binding.etEmail.addTextChangedListener(textWatcher);
        binding.etSignUpUsername.addTextChangedListener(textWatcher);
        binding.etSignUpPassword.addTextChangedListener(textWatcher);
        binding.etName.addTextChangedListener(textWatcher);
        binding.etPhone.addTextChangedListener(textWatcher);
        binding.etZipCode.addTextChangedListener(textWatcher);
        binding.btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean accountExist;
                String username = binding.etSignUpUsername.getText().toString();
                String password = binding.etSignUpPassword.getText().toString();
                String name = binding.etName.getText().toString();
                String email = binding.etEmail.getText().toString();
                String phone = binding.etPhone.getText().toString();
                String zipCode = binding.etZipCode.getText().toString();
                List<ParseUser> userList = new ArrayList<>();
                accountExist = queryUserSameUsername(username);
                // Create the ParseUser
                ParseUser user = new ParseUser();
                //Set core properties
                user.setUsername(username);
                user.setPassword(password);
                //custom properties
                user.put("email", email);
                user.put("name", name);
                user.put("phoneNumber", phone);
                user.put("zipCode", zipCode);
                // Invoke signUpInBackground
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            goLoginActivity();
                            Toast.makeText(SignUpActivity.this, "Account Created. Logging In.", Toast.LENGTH_SHORT).show();
                        } else if (!accountExist) {
                            Toast.makeText(SignUpActivity.this, "Signup failed " + e, Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Signup failed " + e);
                        }
                    }
                });
            }
        });
    }

    private boolean queryUserSameUsername(String username) {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("username", username);
        try {
            if (userQuery.getFirst() != null) {
                Toast.makeText(SignUpActivity.this, "Username Already Exist", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setupFloatingLabelErrorLength(TextInputLayout textInputLayout) {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() > 0 && text.length() > textInputLayout.getCounterMaxLength()) {
                    textInputLayout.setError("Error to long!");
                    textInputLayout.setErrorEnabled(true);
                } else {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }


}