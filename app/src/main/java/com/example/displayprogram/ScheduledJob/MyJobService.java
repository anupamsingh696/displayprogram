package com.example.displayprogram.ScheduledJob;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyJobService extends Service {
    final String MY_ACTION = "MY_ACTION";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("MyJobService :","onStartCommand");
        Intent intentBroadCast = new Intent();
        intentBroadCast.setAction(MY_ACTION);
        sendBroadcast(intentBroadCast);

        return START_NOT_STICKY;
    }
}
