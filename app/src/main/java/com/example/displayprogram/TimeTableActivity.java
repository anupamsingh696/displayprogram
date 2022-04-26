package com.example.displayprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import com.example.displayprogram.Adapter.AdapterTimeTable;
import com.example.displayprogram.DB.DBHandler;
import com.example.displayprogram.Model.ModelClass;

import java.util.ArrayList;

public class TimeTableActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private Context mContext;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_time_table);

        recyclerView = findViewById(R.id.rvTimetable);

        mContext=this;
        dbHandler = new DBHandler(mContext);
        fetchLocalDB();
    }

    private void fetchLocalDB(){
        if(dbHandler==null){
            dbHandler = new DBHandler(mContext);
        }
        // fetch local db data
        ArrayList<ModelClass> listTimeTableFromDB = dbHandler.getAllDataFromSQLiteDB();

        reloadTimeTable(listTimeTableFromDB);
    }

    private void reloadTimeTable(ArrayList<ModelClass> listTimeTable) {
        AdapterTimeTable adapter = new AdapterTimeTable(listTimeTable);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
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