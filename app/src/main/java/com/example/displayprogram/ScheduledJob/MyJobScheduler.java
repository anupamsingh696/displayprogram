package com.example.displayprogram.ScheduledJob;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobScheduler extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e("MyJobScheduler : ","onStart");
        Intent service = new Intent(getApplicationContext(), MyJobService.class);
        getApplicationContext().startService(service);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
