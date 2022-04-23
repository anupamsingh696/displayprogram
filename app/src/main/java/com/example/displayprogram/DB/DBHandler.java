package com.example.displayprogram.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.displayprogram.Model.ModelClass;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "displayprogram";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "timetable";

    // below variable is for our id column.
    private static final String COL_ID = "id";
    private static final String COL_UNIT_CODE = "unitcode";
    private static final String COL_UNIT_NAME = "unitname";
    private static final String COL_CLASS_NO = "classno";
    private static final String COL_TEACHER_NAME = "teachername";
    private static final String COL_SCHEDULE_STATUS = "schedulestatus";
    private static final String COL_TRANSACTION_DATE = "transactiondate";
    private static final String COL_START_TIME = "starttime";
    private static final String COL_END_TIME = "endtime";
    private static final String COL_ROOM_CODE = "roomcode";
    private static final String COL_ROOM_NAME = "roomname";
    private static final String COL_ROOM_SIZE = "roomsize";
    private static final String COL_ROOM_CAPACITY = "roomcapacity";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_UNIT_CODE + " TEXT,"
                + COL_UNIT_NAME + " TEXT,"
                + COL_CLASS_NO + " TEXT,"
                + COL_TEACHER_NAME + " TEXT,"
                + COL_SCHEDULE_STATUS + " TEXT,"
                + COL_TRANSACTION_DATE + " TEXT,"
                + COL_START_TIME + " TEXT,"
                + COL_END_TIME + " TEXT,"
                + COL_ROOM_CODE + " TEXT,"
                + COL_ROOM_NAME + " TEXT,"
                + COL_ROOM_SIZE + " TEXT,"
                + COL_ROOM_CAPACITY + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addTime(String unitcode, String unitname, String classno, String teachername,
                             String schedulestatus,String transactiondate,String starttime,String endtime,
                             String roomcode,String roomname,String roomsize,String roomcapacity) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_UNIT_CODE, unitcode);
        values.put(COL_UNIT_NAME, unitname);
        values.put(COL_CLASS_NO, classno);
        values.put(COL_TEACHER_NAME, teachername);
        values.put(COL_SCHEDULE_STATUS, schedulestatus);
        values.put(COL_TRANSACTION_DATE, transactiondate);
        values.put(COL_START_TIME, starttime);
        values.put(COL_END_TIME, endtime);
        values.put(COL_ROOM_CODE, roomcode);
        values.put(COL_ROOM_NAME, roomname);
        values.put(COL_ROOM_SIZE, roomsize);
        values.put(COL_ROOM_CAPACITY, roomcapacity);
        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<ModelClass> getAllDataFromSQLiteDB(){
        ArrayList<ModelClass> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ TABLE_NAME,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ModelClass mc = new ModelClass();

                mc.setUnitcode(cursor.getString(cursor.getColumnIndex(COL_UNIT_CODE)));
                mc.setUnitname(cursor.getString(cursor.getColumnIndex(COL_UNIT_NAME)));
                mc.setClassno(cursor.getString(cursor.getColumnIndex(COL_CLASS_NO)));
                mc.setTeachername(cursor.getString(cursor.getColumnIndex(COL_TEACHER_NAME)));
                mc.setSchedulestatus(cursor.getString(cursor.getColumnIndex(COL_SCHEDULE_STATUS)));
                mc.setTransactiondate(cursor.getString(cursor.getColumnIndex(COL_TRANSACTION_DATE)));
                mc.setStarttime(cursor.getString(cursor.getColumnIndex(COL_START_TIME)));
                mc.setEndtime(cursor.getString(cursor.getColumnIndex(COL_END_TIME)));
                mc.setRoomcode(cursor.getString(cursor.getColumnIndex(COL_ROOM_CODE)));
                mc.setRoomname(cursor.getString(cursor.getColumnIndex(COL_ROOM_NAME)));
                mc.setRoomsize(cursor.getString(cursor.getColumnIndex(COL_ROOM_SIZE)));
                mc.setRoomcapacity(cursor.getString(cursor.getColumnIndex(COL_ROOM_CAPACITY)));

                list.add(mc);
                cursor.moveToNext();
            }
        }

        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteAllRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }
}
