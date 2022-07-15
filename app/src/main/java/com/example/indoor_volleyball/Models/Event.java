package com.example.indoor_volleyball.Models;

import android.content.Intent;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Event")
public class Event extends ParseObject {
    public static final String TAG = "EVENT";
    public static final String KEY_EVENTCODE = "eventCode";
    public static final String KEY_GYM = "gym";
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_ALLOWPLUSONES = "allowPlusOnes";
    public static final String KEY_ALLOWSPECTATORS = "allowSpectators";
    public static final String KEY_MINCOUNT = "minCount";
    public static final String KEY_MAXCOUNT = "maxCount";
    public static final String KEY_WAITLIST = "waitList";
    public static final String KEY_STARTTIME = "startTime";
    public static final String KEY_ENDTIME = "endTime";
    public static final String KEY_SKILLLEVEL = "skillLevel";
    public static final String KEY_DETAILS = "details";
    public static final String KEY_TEAMROTATION = "teamRotation";
    public static final String KEY_USERRELATION = "userRelation";

    public String getEventCode() {
        return getString(KEY_EVENTCODE);
    }

    public void setEventCode(String eventCode) {
        put(KEY_EVENTCODE, eventCode);
    }

    public Gym getGym() {
        return (Gym) getParseObject(KEY_GYM);
    }

    public void setGym(ParseObject gym) {
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

    public Date getStartTime() {
        try {
            return fetchIfNeeded().getDate(KEY_STARTTIME);
        } catch (ParseException e) {
            Log.e(TAG, "Error with Start Time" + e);
        }
        return null;
    }

    public void setStartTime(Date startTime) {
        put(KEY_STARTTIME, startTime);
    }

    public Date getEndTime() {
        try {
            return fetchIfNeeded().getDate(KEY_ENDTIME);
        } catch (ParseException e) {
            Log.e(TAG, "Error with End Time" + e);
        }
        return null;
    }

    public void setEndTime(Date endTime) {
        put(KEY_ENDTIME, endTime);
    }

    public String getSkillLevel() {
        return getString(KEY_SKILLLEVEL);
    }

    public void setSkillLevel(String skillLevel) {
        put(KEY_SKILLLEVEL, skillLevel);
    }

    public String getDetails() {
        try {
            return fetchIfNeeded().getString(KEY_DETAILS);
        } catch (ParseException e) {
            Log.e(TAG, "Error with event details" + e);
        }
        return "";
    }

    public void setDetails(String details) {
        put(KEY_DETAILS, details);
    }

    public String getTeamRotation() {
        return getString(KEY_TEAMROTATION);
    }

    public void setTeamRotation(String teamRotation) {
        put(KEY_TEAMROTATION, teamRotation);
    }

    public ParseRelation getUserRelation() {
        return getRelation(KEY_USERRELATION);
    }

    public void setUserRelation(ParseRelation userRelation) {
        put(KEY_USERRELATION, userRelation);
    }

}
