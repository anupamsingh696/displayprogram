package com.example.displayprogram;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.displayprogram.DB.DBHandler;
import com.example.displayprogram.Model.ModelClass;
import com.example.displayprogram.Utils.CommonFunction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ScheduledActivity extends AppCompatActivity {
    TextView tvFromTime, tvToTime, tvInfo, tvClassId, tvDate, tvTime, tvTeacherName, tvRemarks;
    String strFromTime = "";
    String strToTime = "";
    private String strCurrentDate = "";
    private String strCurrentTime = "";
    private Context mContext;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scheduled);

        mContext = this;
        dbHandler = new DBHandler(mContext);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        strCurrentDate = sdf.format(new Date());
        Log.e("strCurrentDate : ", strCurrentDate);

        SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        strCurrentTime = sdfT.format(new Date());
        Log.e("strCurrentTime : ", strCurrentTime);


        tvFromTime = findViewById(R.id.tvFromTime);
        tvToTime = findViewById(R.id.tvToTime);
        tvInfo = findViewById(R.id.tvInfo);
        tvClassId = findViewById(R.id.tvClassId);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvTeacherName = findViewById(R.id.tvTeacherName);
        tvRemarks = findViewById(R.id.tvRemarks);

        tvInfo.setText("-");
        tvClassId.setText("-");
        tvDate.setText("-");
        tvTime.setText("-");
        tvTeacherName.setText("-");
        tvRemarks.setText("-");

    }
    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    public void onClick1(View view) {
        switch (view.getId()) {
            case R.id.tvFromTime:
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);

                 TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduledActivity.this,
                        (view12, hourOfDay, minute12) -> {
                            tvFromTime.setText(hourOfDay + ":" + minute12);
                            strFromTime = tvFromTime.getText().toString().trim();
                            if(strToTime.isEmpty()){
                                Toast.makeText(mContext,"Select To Time",Toast.LENGTH_LONG).show();
                            }else {
                                fetchLocalDB();
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
                break;
            case R.id.tvToTime:
                Calendar mCurrentTime1 = Calendar.getInstance();
                int hour1 = mCurrentTime1.get(Calendar.HOUR_OF_DAY);
                int minute1 = mCurrentTime1.get(Calendar.MINUTE);

                 TimePickerDialog timePickerDialog1 = new TimePickerDialog(ScheduledActivity.this,
                        (view1, hourOfDay, minute11) -> {
                            tvToTime.setText(hourOfDay + ":" + minute11);
                            strToTime = tvToTime.getText().toString().trim();
                            if(strFromTime.isEmpty()){
                                Toast.makeText(mContext,"Select From Time",Toast.LENGTH_LONG).show();
                            }else {
                                fetchLocalDB();
                            }
                        }, hour1, minute1, false);
                timePickerDialog1.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
         super.onBackPressed();
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Do nothing or catch the keys you want to block
        return false;
    }*/
    private void fetchLocalDB(){
        // fetch local db data
        ArrayList<ModelClass> listTimeTableFromDB = dbHandler.getAllDataFromSQLiteDB();
        setUIData(listTimeTableFromDB);
    }

    @SuppressLint("SetTextI18n")
    private void setUIData(ArrayList<ModelClass> timeTableResponses){
        tvInfo.setText("-");
        tvClassId.setText("-");
        tvDate.setText("-");
        tvTime.setText("-");
        tvTeacherName.setText("-");
        tvRemarks.setText("-");

        for (int x = 0; x < timeTableResponses.size(); x++) {
            if (timeTableResponses.get(x).getTransactiondate() != null) {
                if (timeTableResponses.get(x).getTransactiondate().trim().equals(strCurrentDate)) {
                    //String strConcatTime = strCurrentTime.split(":")[0].concat(strCurrentTime.split(":")[1]);
                    String strConcatTime = strFromTime.split(":")[0].concat(strFromTime.split(":")[1]);;
                    Log.e("strConcatTime : ", strConcatTime);
                    String strStartTime = timeTableResponses.get(x).getStarttime();
                    String strEndTime = timeTableResponses.get(x).getEndtime();
                    if (strStartTime != null && strEndTime != null) {
                        if (Integer.parseInt(strConcatTime) > Integer.parseInt(strStartTime) && Integer.parseInt(strConcatTime) < Integer.parseInt(strEndTime)) {

                            tvInfo.setText(timeTableResponses.get(x).getUnitcode() + ", " + timeTableResponses.get(x).getUnitname());
                            tvClassId.setText(timeTableResponses.get(x).getClassno());
                            tvDate.setText(CommonFunction.getDateInDDMMMMYYYY(timeTableResponses.get(x).getTransactiondate()));
                            tvTime.setText(CommonFunction.timeConvert(timeTableResponses.get(x).getStarttime() )+ " - " + CommonFunction.timeConvert(timeTableResponses.get(x).getEndtime()));
                            tvTeacherName.setText(timeTableResponses.get(x).getTeachername());
                            tvRemarks.setText(timeTableResponses.get(x).getSchedulestatus());

                        }
                    }
                }
            }
        }
    }

}