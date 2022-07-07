package com.example.indoor_volleyball.Models;

import android.content.Intent;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Gym")
public class Gym extends ParseObject {
    //TODO Serializable isn't ideal but I am leaving it for later cleanup since there are no current noticeable performance issues.
    public static final String TAG ="GYM";

    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PLACEID = "placeId";
    public static final String KEY_OPENINGHOURS = "openingHours";
    public static final String KEY_PHONENUMBER = "phoneNumber";
    public static final String KEY_WEBSITEURL = "websiteUrl";
    public static final String KEY_RATING = "rating";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_NEXTEVET = "nextEvent";
//TODO add the fetch if needed and try catch to all getters in case of parse bullshit.

    public String getName() {
        try {
            return fetchIfNeeded().getString(KEY_NAME);
        } catch (ParseException e) {
            Log.e(TAG, "Error with gym name" + e);
        }
        return "";
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


    public void setOpeningHours(JSONArray array) {
        put(KEY_OPENINGHOURS, array);
    }
    public JSONArray getOpeningHours() {
        return getJSONArray(KEY_OPENINGHOURS);
    }
    public void setPlaceId(String placeId) {
        put(KEY_PLACEID, placeId);
    }
    public String getPlaceId() {
        return getString(KEY_PLACEID);
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

    public Number getRating() {
        return getNumber(KEY_RATING);
    }

    public void setRating(Number rating) {
        put(KEY_RATING, rating);
    }

    public ParseGeoPoint getLocation () {
        return getParseGeoPoint(KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint location) {
        put(KEY_LOCATION, location);
    }

    public Event getNextEvent() {
        return (Event) getParseObject(KEY_NEXTEVET);
    }

    public void setNextEvent(Event nextEvent) {
        put(KEY_NEXTEVET, nextEvent);
    }




}
