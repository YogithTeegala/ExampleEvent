package com.example.yogith.exampleevent;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    int date, time, sameDate=0, currentDate, eventDate, currentTime, eventTime, listDateIndex=0, listTimeIndex=0;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.addNotification();
    }
    public void addButton(View view) {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }
    public void viewButton(View view) {
        Intent intent = new Intent(this, ViewEventActivity.class);
        startActivity(intent);
    }
    public void addNotification(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 23);
        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
        sdf.setTimeZone(TimeZone.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        String s[] = currentDateAndTime.split("_");
        currentDate = Integer.valueOf(s[0]);
        currentTime = Integer.valueOf(s[1]);
        //Toast.makeText(MainActivity.this, "CurrentTime: "+currentTime+" CurrentDate: "+currentDate, Toast.LENGTH_LONG).show();
        this.eventFinder();

        Intent myIntent = new Intent(MainActivity.this , AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, myIntent, PendingIntent.FLAG_UPDATE_CURRENT );
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*1000 , pendingIntent);
    }
    public void eventFinder(){
        DBHelper dBHelper = new DBHelper(this);
        Cursor data = dBHelper.fetchData();
        ArrayList<Integer> listDate = new ArrayList();
        while(data.moveToNext()){
            listDate.add(data.getInt(2));
        }
        Collections.sort(listDate);
        for(int i=1; i<listDate.size();i++){
            if(listDate.get(i)>=currentDate)
            {
                listDateIndex = i;
                break;
            }
            else
                eventDate = 0;
        }
        for(int i=listDateIndex+1; i<listDate.size();i++){
            if(listDate.get(listDateIndex).equals(listDate.get(i))){
                sameDate++;
            }
        }
        data.moveToFirst();

        while (data.moveToNext()) {
            date = data.getInt(2);
            if (date==listDate.get(listDateIndex)) {

                    if (sameDate > 0) {
                        ArrayList<Integer> listTime = new ArrayList();
                        data.moveToFirst();
                        while (data.moveToNext()) {
                            listTime.add(data.getInt(3));
                        }
                        Collections.sort(listTime);
                        for(int i=1; i<listTime.size();i++){
                            if(listTime.get(i)>=currentTime)
                            {
                                listTimeIndex = i;
                                break;
                            }
                            else{
                                eventTime = 0;
                            }
                        }
                        data.moveToFirst();
                        while (data.moveToNext()) {
                            time = data.getInt(3);
                            if (time==listTime.get(listTimeIndex)) {
                                title = data.getString(1);
                                //Toast.makeText(MainActivity.this, title + " " + date + " " + time +"list", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                        break;
                    }
                    title = data.getString(1);
                    time = data.getInt(3);
                    //Toast.makeText(MainActivity.this, title + " " + date + " " + time, Toast.LENGTH_LONG).show();
                    break;



            }


        }
    }
}

