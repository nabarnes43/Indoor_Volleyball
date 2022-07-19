package com.example.indoor_volleyball;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.indoor_volleyball.Models.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EventTodayReminderWorker extends Worker {
    private static final String CHANNEL_ID = "Events";
    List<Event> allEvents;
    List<Event> eventsToday;
    Event eventToday;

    public EventTodayReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        //TODO query if there is an event today that the user is attending
        try {
            queryAllEvents();
            if (eventToday != null) {
                //Data eventIdData = new Data.Builder().putString("eventId", eventToday.getObjectId()).build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                // notificationId is a unique int for each notification that you must define
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_event_24)
                        .setContentTitle("You Have An Event Today!")
                        .setContentText("Event Start Time: " + eventToday.getStartTime() + " Event Details: " + eventToday.getDetails())
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Event Start Time: " + eventToday.getStartTime() + " Event Details: " + eventToday.getDetails()))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(1, builder.build());
                return Result.success();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Tag", e.toString());
        }
        return Result.failure();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void queryAllEvents() throws ParseException {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        allEvents = new ArrayList<>();
        eventsToday = new ArrayList<>();
        allEvents.addAll(query.find());
        for (Event event : allEvents) {
            int today = LocalDate.now().getDayOfYear();
            LocalDate eventDate = event.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int eventDayOfYear = eventDate.getDayOfYear();
            if (eventDayOfYear == today) {
                eventsToday.add(event);
                eventToday = event;
            }
        }
    }
}
