package com.example.displayprogram;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.displayprogram.Adapter.AdapterClassStatus;
import com.example.displayprogram.DB.DBHandler;
import com.example.displayprogram.Model.ModelClass;
import com.example.displayprogram.Network.Api;
import com.example.displayprogram.Network.Response.CheckPasswordResponse;
import com.example.displayprogram.Network.Response.ServerTimeResponse;
import com.example.displayprogram.ScheduledJob.MyJobScheduler;
import com.example.displayprogram.Utils.CommonFunction;
import com.example.displayprogram.Utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity {
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_STATE = 2;
    RecyclerView recyclerView;
    TextView tvRoomNo, tvRoomSize, tvRoomCapacity, tvInfo, tvClassId, tvDate, tvTime, tvTeacherName, tvRemarks;
    MyJobReceive myJobReceive;
    SessionManager sessionManager;
    String strFromTime = "";
    String strToTime = "";
    private String strCurrentDate = "";
    private String strCurrentTime = "";
    private String strDeviceId = "";
    private Context mContext;
    private ProgressDialog progress;
    private DBHandler dbHandler;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvClassStatus);
        tvRoomNo = findViewById(R.id.tvRoomNo);
        tvRoomSize = findViewById(R.id.tvRoomSize);
        tvRoomCapacity = findViewById(R.id.tvRoomCapacity);
        tvInfo = findViewById(R.id.tvInfo);
        tvClassId = findViewById(R.id.tvClassId);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvTeacherName = findViewById(R.id.tvTeacherName);
        tvRemarks = findViewById(R.id.tvRemarks);

        init();
        scheduleJob();
    }

    private void init() {
        mContext = this;
        sessionManager = new SessionManager(mContext);
        dbHandler = new DBHandler(mContext);

        // Get Current Date and time

        updateDateTime();

        // update UI

        updateUI();

        // Progress Dialog Initialize

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);

        checkStartEndTimeSavedInPhone();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int permissionCheckRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_STATE);
        } else {
            strDeviceId = CommonFunction.getDeviceId(this);
            Log.e("strDeviceId : ", strDeviceId);
            syncServerTime();
        }
    }

    public void syncServerTime() {
        updateDateTime();
        if (CommonFunction.isNetworkConnected(mContext)) {
            progress.show();
            Api.getClient().SyncServerTime(strCurrentDate + " " + strCurrentTime, new Callback<ServerTimeResponse>() {
                @Override
                public void success(ServerTimeResponse serverTimeResponse, Response response) {
                    progress.dismiss();
                    Log.e("serverDateTime : ", serverTimeResponse.getResponse());
                    Log.e("serverDate : ", serverTimeResponse.getResponse().split(" ")[0]);
                    Log.e("serverTime : ", serverTimeResponse.getResponse().split(" ")[1]);
                    if (strCurrentDate.trim().equals(serverTimeResponse.getResponse().split(" ")[0].trim())) {
                        // if (strCurrentTime.split(":")[0].equals(serverTimeResponse.getResponse().split(" ")[1].split(":")[0])) {
                        checkTimeTable();
                        /*} else {
                            CommonFunction.showMessageInDialog(mContext, "Please set correct time in device.");
                        }*/
                    } else {
                        CommonFunction.showMessageInDialog(mContext, "Please set correct date in device.");
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("error : ", error.getMessage());
                    progress.dismiss();
                }
            });
        } else {
            CommonFunction.networkErrorMessage(mContext);
            checkStartEndTimeSavedInPhone();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
            case REQUEST_READ_EXTERNAL_STORAGE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    int permissionCheckRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_STATE);
                    } else {
                        strDeviceId = CommonFunction.getDeviceId(this);
                        Log.e("strDeviceId : ", strDeviceId);
                        syncServerTime();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void onClick(View view) {
        showPasswordDialog(view);
    }

    private void showPasswordDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.passwordview, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        TextView tvTime = dialogView.findViewById(R.id.tvTime);
        EditText etPassword = dialogView.findViewById(R.id.etPassword);
        btnSubmit.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            if (CommonFunction.isNetworkConnected(mContext)) {
                progress.show();
                Api.getClient().CheckPassword(etPassword.getText().toString().trim(), new Callback<CheckPasswordResponse>() {
                    @Override
                    public void success(CheckPasswordResponse checkPasswordResponse, Response response) {
                        progress.dismiss();
                        Log.e("PasswordResponse : ", checkPasswordResponse.getResponse());
                        if (checkPasswordResponse.getResponse().equals("Correct password")) {
                            showCustomView(view1);
                        } else {
                            CommonFunction.showMessageInDialog(mContext, checkPasswordResponse.getResponse());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progress.dismiss();
                    }
                });
            } else {
                CommonFunction.networkErrorMessage(mContext);
            }
        });

        new CountDownTimer(10000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                tvTime.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                tvTime.setText("0");
                alertDialog.dismiss();
            }

        }.start();

    }

    private void showCustomView(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.customview, viewGroup, false);
        builder.setView(dialogView);
        Button btnTimeTable = dialogView.findViewById(R.id.btnTimeTable);
        Button btnSchedule = dialogView.findViewById(R.id.btnSchedule);
        Button btnExit = dialogView.findViewById(R.id.btnExit);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnExit.setOnClickListener(view13 -> {
            alertDialog.dismiss();
            finish();
        });
        btnTimeTable.setOnClickListener(view12 -> startActivity(new Intent(mContext, TimeTableActivity.class)));
        btnSchedule.setOnClickListener(view1 -> startActivity(new Intent(mContext, ScheduledActivity.class)));
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Do nothing or catch the keys you want to block
        return false;
    }

    private void checkTimeTable() {
        if (CommonFunction.isNetworkConnected(mContext)) {
            Log.e("strDeviceId pass: ", strDeviceId);
            progress.show();
            Api.getClient().CheckTimeTable(strDeviceId, new Callback<ArrayList<ModelClass>>() {

                @Override
                public void success(ArrayList<ModelClass> timeTableResponses, Response response) {
                    progress.dismiss();
                    Log.e("timeTableRes : ", "" + timeTableResponses.size());
                    if (timeTableResponses.size() > 0) {
                        addNewRecordsInSQLiteDB(timeTableResponses);
                    } else {
                        Log.e("timeTableRes : ", "0");
                        CommonFunction.showMessageInDialog(mContext, "No records found using the bellow device id. \n" + strDeviceId);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    progress.dismiss();
                }
            });
        } else {
            CommonFunction.networkErrorMessage(mContext);
            checkStartEndTimeSavedInPhone();
        }
    }

    private void reloadTimeTable(ArrayList<ModelClass> listTimeTable) {
        if(listTimeTable.size()>0) {
            ArrayList<ModelClass> listTimeTable1 = upToThreeData(listTimeTable);
            AdapterClassStatus adapter = new AdapterClassStatus(listTimeTable1);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }

    private void addNewRecordsInSQLiteDB(ArrayList<ModelClass> listTimeTable) {
        dbHandler.deleteAllRecords();
        for (int x = 0; x < listTimeTable.size(); x++) {
            String unitcode = listTimeTable.get(x).getUnitcode();
            String unitname = listTimeTable.get(x).getUnitname();
            String classno = listTimeTable.get(x).getClassno();
            String teachername = listTimeTable.get(x).getTeachername();
            String schedulestatus = listTimeTable.get(x).getSchedulestatus();
            String transactiondate = listTimeTable.get(x).getTransactiondate();
            String starttime = listTimeTable.get(x).getStarttime();
            String endtime = listTimeTable.get(x).getEndtime();
            String roomcode = listTimeTable.get(x).getRoomcode();
            String roomname = listTimeTable.get(x).getRoomname();
            String roomsize = listTimeTable.get(x).getRoomsize();
            String roomcapacity = listTimeTable.get(x).getRoomcapacity();
            Log.e("date :", transactiondate);
            Log.e("start time  :", starttime);
            Log.e("end time :", endtime);

            dbHandler.addTime(unitcode, unitname, classno, teachername, schedulestatus, transactiondate, starttime, endtime, roomcode, roomname, roomsize, roomcapacity);
        }

        ArrayList<ModelClass> listTimeTableFromDB = dbHandler.getAllDataFromSQLiteDB();
        checkStartEndTimeSavedInPhone();
        reloadTimeTable(listTimeTableFromDB);

    }

    @SuppressLint("SetTextI18n")
    private void setUIDataUsingStartTimeEndTime(ArrayList<ModelClass> timeTableResponses) {
        boolean isDataFound = false;
        for (int x = 0; x < timeTableResponses.size(); x++) {
            if (timeTableResponses.get(x).getTransactiondate() != null) {
                if (timeTableResponses.get(x).getTransactiondate().trim().equals(strCurrentDate)) {
                    String strConcatCurrentTime = strCurrentTime.split(":")[0].concat(strCurrentTime.split(":")[1]);
                    Log.e("strConcatCurrentTime : ", strConcatCurrentTime);
                    String strStartTime = timeTableResponses.get(x).getStarttime();
                    String strEndTime = timeTableResponses.get(x).getEndtime();
                    if (strStartTime != null && strEndTime != null) {
                        if (Integer.parseInt(strConcatCurrentTime) > Integer.parseInt(strStartTime) && Integer.parseInt(strConcatCurrentTime) < Integer.parseInt(strEndTime)) {
                           isDataFound = true;
                            tvRoomNo.setText("ROOM " + timeTableResponses.get(x).getRoomcode());
                            tvRoomSize.setText("Room Size : " + timeTableResponses.get(x).getRoomsize());
                            tvRoomCapacity.setText("Capacity : " + timeTableResponses.get(x).getRoomcapacity());
                            tvInfo.setText(timeTableResponses.get(x).getUnitcode() + ", " + timeTableResponses.get(x).getUnitname());
                            tvClassId.setText(timeTableResponses.get(x).getClassno());
                            tvDate.setText(CommonFunction.getDateInDDMMMMYYYY(timeTableResponses.get(x).getTransactiondate()));
                            tvTime.setText(CommonFunction.timeConvert(timeTableResponses.get(x).getStarttime()) + " - " + CommonFunction.timeConvert(timeTableResponses.get(x).getEndtime()));
                            tvTeacherName.setText(timeTableResponses.get(x).getTeachername());
                            tvRemarks.setText(timeTableResponses.get(x).getSchedulestatus());

                        }
                    }
                }
            }
        }

        if(!isDataFound){
            updateUI();
        }
    }

    private void fetchLocalDB(boolean isMatchStartEbdTime) {
        // fetch local db data
        ArrayList<ModelClass> listTimeTableFromDB = dbHandler.getAllDataFromSQLiteDB();
        if (isMatchStartEbdTime) {
            setUIDataUsingStartTimeEndTime(listTimeTableFromDB);
        }
        reloadTimeTable(listTimeTableFromDB);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob() {
        JobScheduler jobScheduler = (JobScheduler) mContext.getSystemService(JOB_SCHEDULER_SERVICE);

        @SuppressLint("JobSchedulerService") ComponentName componentName = new ComponentName(this, MyJobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(15 * 60 * 1000)
                .build();
        jobScheduler.schedule(jobInfo);
    }

    @Override
    protected void onStart() {
        myJobReceive = new MyJobReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MY_ACTION");
        registerReceiver(myJobReceive, intentFilter);

        super.onStart();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myJobReceive);
        super.onDestroy();
    }

    private void checkStartEndTimeSavedInPhone() {
        if (!sessionManager.getStartTime().equals("") && !sessionManager.getEndTime().equals("")) {
            strFromTime = sessionManager.getStartTime();
            strToTime = sessionManager.getEndTime();
            Log.e("start : ", strFromTime);
            Log.e("end : ", strToTime);
            String strConcatStartTime = strFromTime.split(":")[0].concat(strFromTime.split(":")[1]);
            String strConcatEndTime = strToTime.split(":")[0].concat(strToTime.split(":")[1]);
            String strConcatTime = strCurrentTime.split(":")[0].concat(strCurrentTime.split(":")[1]);

            if (Integer.parseInt(strConcatTime) > Integer.parseInt(strConcatStartTime) && Integer.parseInt(strConcatTime) < Integer.parseInt(strConcatEndTime)) {
                fetchLocalDB(true);
            } else {
                updateUI();
            }
        } else {
            fetchLocalDB(false);
        }
    }

    private void updateDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        strCurrentDate = sdf.format(new Date());
        Log.e("strCurrentDate : ", strCurrentDate);

        SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        strCurrentTime = sdfT.format(new Date());
        Log.e("strCurrentTime : ", strCurrentTime);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {
            tvRoomNo.setText("");
            tvRoomSize.setText("Room Size :  ");
            tvRoomCapacity.setText("Capacity : ");
            tvInfo.setText("-");
            tvClassId.setText("-");
            tvDate.setText("-");
            tvTime.setText("-");
            tvTeacherName.setText("-");
            tvRemarks.setText("-");
    }

    @Override
    protected void onResume() {
        FullScreenCall();
        super.onResume();
    }

    public void FullScreenCall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public class MyJobReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MyJobReceive", "onReceive");
            System.gc();
            syncServerTime();
        }
    }

    private ArrayList<ModelClass> upToThreeData(ArrayList<ModelClass> listTimeTable) {
        Log.e("upToThreeData : ", "listTimeTable : " + listTimeTable.size());

        ArrayList<ModelClass> listTimeTableFinal = new ArrayList<>();
        ArrayList<ModelClass> listPast = new ArrayList<>();
        ArrayList<ModelClass> listCurrent = new ArrayList<>();
        ArrayList<ModelClass> listFuture = new ArrayList<>();

        String strConcatCurrentTime = strCurrentTime.split(":")[0].concat(strCurrentTime.split(":")[1]);

        Log.e("upToThreeData : ", "strConcatCurrentTime : " + strConcatCurrentTime);
        Log.e("upToThreeData : ", "strCurrentDate : " + strCurrentDate);

        for (int x = 0; x < listTimeTable.size(); x++) {

            String strStartTime = listTimeTable.get(x).getStarttime();
            String strEndTime = listTimeTable.get(x).getEndtime();

            if (listTimeTable.get(x).getTransactiondate().trim().equals(strCurrentDate)) {
                Log.e("upToThreeData : ", "time  : " + strStartTime + " - " + strEndTime);

                if (Integer.parseInt(strConcatCurrentTime) > Integer.parseInt(strStartTime) && Integer.parseInt(strConcatCurrentTime) > Integer.parseInt(strEndTime)) {
                    listPast.add(listTimeTable.get(x));
                }  else if (Integer.parseInt(strConcatCurrentTime) > Integer.parseInt(strStartTime) && Integer.parseInt(strConcatCurrentTime) < Integer.parseInt(strEndTime)) {
                    listCurrent.add(listTimeTable.get(x));
                } else if (Integer.parseInt(strStartTime) > Integer.parseInt(strConcatCurrentTime)) {
                    listFuture.add(listTimeTable.get(x));
                }
            }
        }

        Log.e("upToThreeData : ", "listPast : " + listPast.size());
        Log.e("upToThreeData : ", "listCurrent : " + listCurrent.size());
        Log.e("upToThreeData : ", "listFuture : " + listFuture.size());

        if (listPast.size() > 0) {
            listTimeTableFinal.add(listPast.get(listPast.size() - 1));
        }
        if (listCurrent.size() > 0) {
            listTimeTableFinal.add(listCurrent.get(listCurrent.size() - 1));
        }
        if (listFuture.size() > 0) {
            if (listTimeTableFinal.size() > 0) {
                if (listTimeTableFinal.size() == 1) {
                    if (listFuture.size() == 1) {
                        listTimeTableFinal.add(listFuture.get(0));
                    } else if (listFuture.size() >= 2) {
                        listTimeTableFinal.add(listFuture.get(0));
                        listTimeTableFinal.add(listFuture.get(1));
                    }
                } else if (listTimeTableFinal.size() == 2) {
                    if (listFuture.size() >= 1) {
                        listTimeTableFinal.add(listFuture.get(0));
                    }
                }
            }else {
                if (listFuture.size() == 1) {
                    listTimeTableFinal.add(listFuture.get(0));
                } else if (listFuture.size() == 2) {
                    listTimeTableFinal.add(listFuture.get(0));
                    listTimeTableFinal.add(listFuture.get(1));
                }else {
                    listTimeTableFinal.add(listFuture.get(0));
                    listTimeTableFinal.add(listFuture.get(1));
                    listTimeTableFinal.add(listFuture.get(2));
                }
            }
        }

        Log.e("upToThreeData : ", "listTimeTableFinal : " + listTimeTableFinal.size());

        return listTimeTableFinal;
    }
}