package com.example.indoor_volleyball;

import android.app.Application;

import com.example.indoor_volleyball.Models.Event;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register parse models
        ParseObject.registerSubclass(Event.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("enjJd2FD8l9X9Pal17iw6ZUZ57HnhhXnuQsGjU1Q")
                .clientKey("L6z6HK2qu1uZ2ePizF0ZaHV9L3dvR6Q6he8kt9PO")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

