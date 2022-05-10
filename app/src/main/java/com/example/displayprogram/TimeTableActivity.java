package com.example.displayprogram;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.displayprogram.Adapter.AdapterTimeTable;
import com.example.displayprogram.DB.DBHandler;
import com.example.displayprogram.Model.ModelClass;
import com.example.displayprogram.Utils.SessionManager;

import java.util.ArrayList;

public class TimeTableActivity extends Activity {
    RecyclerView recyclerView;
    TextView tvHeader;
    private DBHandler dbHandler;
    private Context mContext;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_time_table);

        recyclerView = findViewById(R.id.rvTimetable);
        tvHeader = findViewById(R.id.tvHeader);

        mContext = this;
        dbHandler = new DBHandler(mContext);
        sessionManager = new SessionManager(mContext);
        if(sessionManager.getDeviceId().isEmpty()){
            tvHeader.setText("TimeTable");
        }else {
            tvHeader.setText("TimeTable (Device Id : "+sessionManager.getDeviceId()+" )");
        }

        fetchLocalDB();
    }

    private void fetchLocalDB() {
        if (dbHandler == null) {
            dbHandler = new DBHandler(mContext);
        }
        // fetch local db data
        ArrayList<ModelClass> listTimeTableFromDB = dbHandler.getAllDataFromSQLiteDB();

        reloadTimeTable(listTimeTableFromDB);
    }

    private void reloadTimeTable(ArrayList<ModelClass> listTimeTable) {
        AdapterTimeTable adapter = new AdapterTimeTable(listTimeTable);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Do nothing or catch the keys you want to block
        return false;
    }*/

}