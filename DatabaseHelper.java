package com.example.healthrider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "HealthDashboard.db";
    public static final String TABLE1 = "MAIN_DETAILS";
    public static final String TABLE2 = "WALKING";
    public static final String TABLE3 = "RUNNING";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "WEIGHT";
    public static final String COL_4 = "HEIGHT";
    public static final String COL_5 = "AGE";
    public static final String COL_6 = "GENDER";
    public static final String COL_7 = "DATE";
    public static final String COL_8 = "WALKING_DISTANCE";
    public static final String COL_9 = "RUNNING_DISTANCE";
    public static final String COL_10 = "WALKING_STEPS";
    public static final String COL_11 = "RUNNING_STEPS";
    public static final String COL_12 = "WALKING_CALORIES";
    public static final String COL_13 = "RUNNING_CALORIES";
    public static final String COL_15 = "ID1";
    public static final String COL_16 = "ID2";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String table1 = "CREATE TABLE " + TABLE1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,WEIGHT INTEGER,HEIGHT INTEGER,AGE INTEGER,GENDER TEXT)";
        String table2 = "CREATE TABLE "+TABLE2+" (ID1 INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,DATE TEXT,WALKING_DISTANCE TEXT,WALKING_STEPS TEXT,WALKING_CALORIES TEXT)";
        String table3 = "CREATE TABLE "+TABLE3+" (ID2 INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,DATE TEXT,RUNNING_DISTANCE TEXT,RUNNING_STEPS TEXT,RUNNING_CALORIES TEXT)";


        db.execSQL(table1); db.execSQL(table2); db.execSQL(table3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE1);  db.execSQL("DROP TABLE IF EXISTS "+TABLE2); db.execSQL("DROP TABLE IF EXISTS "+TABLE3);

        onCreate(db);
    }

    public boolean insertData(String name, float weight, float height, float age, String gender){
        String filter = getTable1();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, weight);
        contentValues.put(COL_4, height);
        contentValues.put(COL_5, age);
        contentValues.put(COL_6, gender);

        sqLiteDatabase.insert(filter,null ,contentValues);
        return true;
    }

    public boolean insertTable2(String date, float walking_distance,float walking_steps, float walking_calories, float running_distance, float running_steps, float running_calories){
        String filter = getTable2();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7, date);
        contentValues.put(COL_8, walking_distance);
        contentValues.put(COL_9, running_distance);
        contentValues.put(COL_10, walking_steps);
        contentValues.put(COL_11, running_steps);
        contentValues.put(COL_12, walking_calories);
        contentValues.put(COL_13, running_calories);

        sqLiteDatabase.insert(filter,null ,contentValues);
        return true;
    }


    public Cursor getAllData(String TABLE_NAME) {
        String filter = null;
        if (TABLE_NAME.equals("TABLE1")){filter = getTable1();}else if(TABLE_NAME.equals("TABLE2")){filter = getTable2();}else if(TABLE_NAME.equals("TABLE3")){filter = getTable3();}
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+filter, null);
        //Cursor cursor2 = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE2, null);
        return cursor;
    }

    //assets
    private String getTable1() {
        return TABLE1;
    }
    private String getTable2() {
        return TABLE2;
    }
    private String getTable3() {
        return TABLE3;
    }

}
