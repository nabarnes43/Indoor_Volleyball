package com.example.indoor_volleyball.Models;

import android.content.Intent;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Gym")
public class Gym extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_OPENTIME = "openTime";
    public static final String KEY_CLOSETIME = "closeTime";
    public static final String KEY_PHONENUMBER = "phoneNumber";
    public static final String KEY_WEBSITEURL = "websiteUrl";
    public static final String KEY_RATING = "rating";
    public static final String KEY_LOCATION = "location";


    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }
    public String getAddress() {
        return getString(KEY_ADDRESS);
    }

    public void setAddress(String address) {
        put(KEY_ADDRESS, address);
    }
    public String getOpenTime() {
        return getString(KEY_OPENTIME);
    }

    public void setOpenTime(String openTime) {
        put(KEY_OPENTIME, openTime);
    }
    public String getCloseTime() {
        return getString(KEY_CLOSETIME);
    }

    public void setCloseTime(String closeTime) {
        put(KEY_CLOSETIME, closeTime);
    }
    public String getPhoneNumber() {
        return getString(KEY_PHONENUMBER);
    }

    public void setPhoneNumber(String phoneNumber) {
        put(KEY_PHONENUMBER, phoneNumber);
    }
    public String getWebsiteUrl() {
        return getString(KEY_WEBSITEURL);
    }

    public void setWebsiteUrl(String websiteUrl) {
        put(KEY_WEBSITEURL, websiteUrl);
    }

    public int getRating() {
        return getInt(KEY_RATING);
    }

    public void setRating(String rating) {
        put(KEY_RATING, rating);
    }

    public ParseGeoPoint getLocation () {
        return getParseGeoPoint(KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint location) {
        put(KEY_LOCATION, location);
    }


}
