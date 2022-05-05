package com.example.displayprogram.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CommonFunction {

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        if(deviceId==null || deviceId.isEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deviceId = Build.getSerial();
            }
        }


        return deviceId;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void networkErrorMessage(Context context) {
        Toast.makeText(context, "Network not available in device", Toast.LENGTH_LONG).show();
    }

    public static void showMessageInDialog(Context context, String string) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(string);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateInDDMMMMYYYY(String strDate) {
        String reformattedStr = "";
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MMMM yyyy");

        try {
            reformattedStr = myFormat.format(Objects.requireNonNull(fromUser.parse(strDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }
    @SuppressLint("SimpleDateFormat")
    public static String getDateInDDMMYYYY(String strDate) {
        String reformattedStr = "";
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            reformattedStr = myFormat.format(Objects.requireNonNull(fromUser.parse(strDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }
    @SuppressLint("SimpleDateFormat")
    public static String timeConvert(String strTime) {
        String time = "";
        try {
            String _24HourTime = strTime;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HHmm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            System.out.println(_24HourDt);
            System.out.println(_12HourSDF.format(_24HourDt));
            time = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
}
