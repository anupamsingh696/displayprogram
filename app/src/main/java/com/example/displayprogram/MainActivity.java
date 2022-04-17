package com.example.displayprogram;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.displayprogram.Adapter.AdapterClassStatus;
import com.example.displayprogram.Model.ModelClass;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ArrayList<ModelClass> myListData = new ArrayList<>();

        ModelClass m1 = new ModelClass();
        m1.setUnitCode("MGMT2004");
        m1.setUnitName("Business & Sustainable Development");
        m1.setTimeFrame("10:00 am - 12:00 pm");
        m1.setRemarks("Cancelled");
        myListData.add(m1);

        ModelClass m2 = new ModelClass();
        m2.setUnitCode("MGMT2004");
        m2.setUnitName("Business & Sustainable Development");
        m2.setTimeFrame("10:00 am - 12:00 pm");
        m2.setRemarks("Cancelled");
        myListData.add(m2);

        ModelClass m3 = new ModelClass();
        m3.setUnitCode("MGMT2004");
        m3.setUnitName("Business & Sustainable Development");
        m3.setTimeFrame("10:00 am - 12:00 pm");
        m3.setRemarks("Cancelled");
        myListData.add(m3);

        RecyclerView recyclerView = findViewById(R.id.rvClassStatus);
        AdapterClassStatus adapter = new AdapterClassStatus(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public void onClick(View view){
        showPasswordDialog(view);
    }

    private void showPasswordDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.passwordview, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        TextView tvTime = dialogView.findViewById(R.id.tvTime);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                showCustomView(view);
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
    private void showCustomView(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.customview, viewGroup, false);
        builder.setView(dialogView);
        Button btnTimeTable = dialogView.findViewById(R.id.btnTimeTable) ;
        Button btnSchedule = dialogView.findViewById(R.id.btnSchedule) ;
        Button btnExit = dialogView.findViewById(R.id.btnExit) ;
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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
}