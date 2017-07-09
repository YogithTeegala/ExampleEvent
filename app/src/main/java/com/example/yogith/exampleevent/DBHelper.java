package com.example.yogith.exampleevent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Yogith on 01-07-2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME =  "EventManager.db";
    public static final String TABLE_NAME =  "EventManage";
    public static final String Col_1 =  "ID";
    public static final String Col_2 =  "TITLE";
    public static final String Col_3 =  "DATE";
    public static final String Col_4 =  "TIME";
    public DBHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ("+Col_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ Col_2 + " TEXT, " + Col_3+" TEXT, "+Col_4+" TEXT)");
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String title, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2, title);
        contentValues.put(Col_3, date);
        contentValues.put(Col_4, time);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }
    public Cursor fetchData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME, null);
        return res;
    }

    public Cursor getItemID(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+ Col_1 + " from "+ TABLE_NAME + " where " + Col_2 + " = '" + title + "'", null);
        return res;
    }

    public void updateData(String newTitle, String oldTitle, String newDate, String newTime, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+ TABLE_NAME + " SET " +
                Col_2 + "= '"+ newTitle +"', "+
                Col_3 + "= '"+ newDate +"', "+
                Col_4 + "= '"+ newTime + "' WHERE "+
                Col_1 +"= '" + id + "'" + " AND "+
                Col_2 +"= '" + oldTitle + "'");
    }
    public void deleteData(int id, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME + " WHERE "+
                Col_1 + " = '"+id + "'"
        );
    }
}
