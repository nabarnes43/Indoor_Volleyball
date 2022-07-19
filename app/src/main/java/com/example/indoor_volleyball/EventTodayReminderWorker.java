package com.example.indoor_volleyball;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class EventTodayReminderWorker extends Worker {

    public EventTodayReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        //TODO query if there is an event today that the user is attending
        Log.e("TAG" , "Worker is Working");

        return Result.success();
    }
}
