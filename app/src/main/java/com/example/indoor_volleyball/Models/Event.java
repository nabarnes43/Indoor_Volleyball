package com.example.indoor_volleyball.Models;

import android.content.Intent;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("Event")
public class Event extends ParseObject {
    // Ensure that your subclass has a public default constructor
    public static final String KEY_EVENTCODE = "eventCode";
    public static final String KEY_GYM = "gym";
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_ALLOWPLUSONES = "allowPlusOnes";
    public static final String KEY_ALLOWSPECTATORS = "allowSpectators";
    public static final String KEY_MINCOUNT = "minCount";
    public static final String KEY_MAXCOUNT = "maxCount";
    public static final String KEY_WAITLIST = "waitList";
    public static final String KEY_DATE = "date";
    public static final String KEY_STARTTIME = "startTime";
    public static final String KEY_ENDTIME = "endTime";
    public static final String KEY_SKILLLEVEL = "skillLevel";
    public static final String KEY_DETAILS = "details";
    public static final String KEY_QRCODE = "qrCode";
    public static final String KEY_GYMID = "gymId";




    public ParseFile getEventCode() {
        return getParseFile(KEY_EVENTCODE);
    }

    public void setEventCode(ParseFile eventCode) {
        put(KEY_EVENTCODE, eventCode);
    }
    //I don't know about this code
    public ParseObject getGym() {
        return getParseObject(KEY_GYM);
    }

    public void setGym (ParseObject gym) {
        put(KEY_GYM, gym);
    }

    public ParseUser getCreator() {
        return getParseUser(KEY_CREATOR);
    }

    public void setCreator(ParseUser creator) {
        put(KEY_CREATOR, creator);
    }

    public Boolean getAllowPlusOnes() {
        return getBoolean(KEY_ALLOWPLUSONES);
    }

    public void setAllowPlusOnes(Boolean allowPlusOnes) {
        put(KEY_ALLOWPLUSONES, allowPlusOnes);
    }

    public Boolean getAllowSpectators() {
        return getBoolean(KEY_ALLOWSPECTATORS);
    }

    public void setAllowSpectators(Boolean allowSpectators) {
        put(KEY_ALLOWSPECTATORS, allowSpectators);
    }

    public Number getMinCount() {
        return getNumber(KEY_MINCOUNT);
    }

    public void setMinCount(int minCount) {
        put(KEY_MINCOUNT, minCount);
    }
    public Number getMaxCount() {
        return getNumber(KEY_MAXCOUNT);
    }

    public void setMaxCount(int maxCount) {
        put(KEY_MAXCOUNT, maxCount);
    }

    public Number getWaitList() {
        return getNumber(KEY_WAITLIST);
    }

    public void setWaitList(int waitList) {
        put(KEY_WAITLIST, waitList);
    }


    public String getDate() {
        return getString(KEY_DATE);
    }

    public void setDate(String date) {
        put(KEY_DATE, date);
    }
    public String getStartTime() {
        return getString(KEY_STARTTIME);
    }

    public void setStartTime(String startTime) {
        put(KEY_STARTTIME, startTime);
    }

    public String getEndTime() {
        return getString(KEY_ENDTIME);
    }

    public void setEndTime(String endTime) {
        put(KEY_ENDTIME, endTime);
    }

    public String getSkillLevel() {
        return getString(KEY_SKILLLEVEL);
    }

    public void setSkillLevel(String skillLevel) {
        put(KEY_SKILLLEVEL, skillLevel);
    }

    public String getDetails() {
        return getString(KEY_DETAILS);
    }

    public void setKeyDetails(String details) {
        put(KEY_DETAILS, details);
    }

    public String getQrCode() {
        return getString(KEY_QRCODE);
    }

    public void setQrCode(String qrCode) {
        put(KEY_QRCODE, qrCode);
    }

    public String getGymId() {
        return getString(KEY_GYMID);
    }

    public void setGymId(String gymId) {
        put(KEY_GYMID, gymId);
    }




}
