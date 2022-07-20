package com.example.indoor_volleyball;

import android.app.Application;

import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
import com.example.indoor_volleyball.R;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Register parse models
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Gym.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );
        ArrayList<String> channels = new ArrayList<>();
        channels.add("Events");
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "625555484244");
        installation.put("channels", channels);
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();
    }
}

