package com.example.yogith.exampleevent;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yogith on 01-07-2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME =  "EventManager.db";
    public static final String TABLE_NAME =  "EventManager";
    public static final String Col_1 =  "NAME";
    public static final String Col_2 =  "DATE";
    public static final String Col_3 =  "TIME";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (NAME TEXT, DATE TEXT, TIME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String name, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_1, name);
        contentValues.put(Col_2, date);
        contentValues.put(Col_3, time);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }
}
