package com.example.displayprogram.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {
    SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
    }

    public String getStartTime() {
        return sharedPreferences.getString("startTime", "");
    }

    public void setStartTime(String startTime) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("startTime", startTime);
        myEdit.commit();
    }

    public String getEndTime() {
        return sharedPreferences.getString("endTime", "");
    }

    public void setEndTime(String endTime) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("endTime", endTime);
        myEdit.commit();
    }

}
