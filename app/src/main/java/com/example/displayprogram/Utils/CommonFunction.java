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

public class CommonFunction {
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {

        String IME;

       /* if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
        }*/

        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (mTelephony.getPhoneCount() == 2) {
                    IME = mTelephony.getImei(0);
                }else{
                    IME = mTelephony.getImei();
                }
            }else{
                if (mTelephony.getPhoneCount() == 2) {
                    IME = mTelephony.getDeviceId(0);
                } else {
                    IME = mTelephony.getDeviceId();
                }
            }
        } else {
            IME = mTelephony.getDeviceId();
        }

        return IME;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void networkErrorMessage(Context context){
        Toast.makeText(context,"Network not available in device",Toast.LENGTH_LONG).show();
    }

    public static void showMessageInDialog(Context context,String string){
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
}
