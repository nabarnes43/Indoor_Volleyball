package com.example.indoor_volleyball;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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

import com.example.indoor_volleyball.Activities.Details.EventAttendingActivity;
import com.example.indoor_volleyball.Activities.Details.GymDetailActivity;
import com.example.indoor_volleyball.Models.Event;
import com.example.indoor_volleyball.Models.Gym;
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
    private static final int NOTIFICATION_ID = 1;
    private Event eventToday;


    public EventTodayReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        try {
            queryAllEvents();
            if (eventToday != null) {
                // Create an Intent for the activity you want to start
                Intent resultIntent = EventAttendingActivity.newIntent(getApplicationContext(), eventToday.getObjectId());
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                // Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                // notificationId is a unique int for each notification that you must define
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_event_24)
                        .setContentTitle("You Have An Event Today!")
                        .setContentText("Event Start Time: " + eventToday.getStartTime() + " Event Details: " + eventToday.getDetails())
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Event Start Time: " + eventToday.getStartTime() + " Event Details: " + eventToday.getDetails()))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setContentIntent(resultPendingIntent);
                notificationManager.notify(NOTIFICATION_ID, builder.build());
                return Result.success();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Tag", e.toString());
        }
        return Result.failure();
    }

    //TODO query if there is an event today that the user is attending
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void queryAllEvents() throws ParseException {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include("gym");
        query.include("creator");
        List<Event> eventsToday = new ArrayList<>();
        List<Event> allEvents = new ArrayList<>(query.find());
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
