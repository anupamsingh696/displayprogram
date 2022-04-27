package com.example.displayprogram;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.displayprogram.Adapter.AdapterTimeTable;
import com.example.displayprogram.DB.DBHandler;
import com.example.displayprogram.Model.ModelClass;

import java.util.ArrayList;

public class TimeTableActivity extends Activity {
    RecyclerView recyclerView;
    private DBHandler dbHandler;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_time_table);

        recyclerView = findViewById(R.id.rvTimetable);

        mContext = this;
        dbHandler = new DBHandler(mContext);

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