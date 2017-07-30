package com.example.yogith.exampleevent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    int ID, date, sameDate=0, currentDate, eventDate, currentTime, eventTime, currentYear, currentMonth, currentDay, currentHour, currentMin, eventTitle, eventHour, eventMin, eventYear, eventMonth, eventDay, listDateIndex=0, listTimeIndex=0;
    String title, time, currentDateString, currentTimeString, eventDateString, eventTimeString;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
        sdf.setTimeZone(TimeZone.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        String s[] = currentDateAndTime.split("_");
        currentDate = Integer.valueOf(s[0]);
        currentTime = Integer.valueOf(s[1]);

        currentYear = Integer.valueOf(s[0].substring(0, 4));
        currentMonth =  Integer.valueOf(s[0].substring(4, 6));
        currentDay = Integer.valueOf(s[0].substring(6, 8));

        currentTimeString = String.format("%04d", currentTime);
        currentHour = Integer.valueOf(currentTimeString.substring(0, 2));
        currentMin = Integer.valueOf(currentTimeString.substring(2, 4));

        this.eventFinder();

        if(date>=currentDate) {
            eventDateString = String.format("&08d", date);
            eventYear = Integer.valueOf(s[0].substring(0, 4));
            eventMonth =  Integer.valueOf(s[0].substring(4, 6));
            eventDay = Integer.valueOf(s[0].substring(6, 8));

            currentTimeString = String.format("%04d", eventTime);
            eventHour = Integer.valueOf(currentTimeString.substring(0, 2));
            eventMin = Integer.valueOf(currentTimeString.substring(2, 4));
            Toast.makeText(MainActivity.this, "inside if "+eventTime + " Date: " + date + " Title: " + title, Toast.LENGTH_LONG).show();
            calendar.set(Calendar.YEAR, eventYear);
            calendar.set(Calendar.MONTH, eventMonth);
            calendar.set(Calendar.DAY_OF_MONTH,eventDay);

            if(eventTime>=currentTime) {
                calendar.set(Calendar.HOUR_OF_DAY, eventHour);
                calendar.set(Calendar.MINUTE, eventMin);

                Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);

                Toast.makeText(MainActivity.this, "inside if if "+eventTime + " Date: " + date + " Title: " + title, Toast.LENGTH_LONG).show();

//                myIntent.putExtra("title", title);
//                myIntent.putExtra("time", currentTimeString);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
        Toast.makeText(MainActivity.this, eventTime + " Date: " + date + " Title: " + title, Toast.LENGTH_LONG).show();
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
                        listTime.add(Integer.valueOf(data.getString(3)));
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
                        time = data.getString(3);
                        eventTime = Integer.valueOf(time);
                        if (eventTime==listTime.get(listTimeIndex)) {
                            title = data.getString(1);
                                Toast.makeText(MainActivity.this, title + "inside while if " + date + " " + time +" "+listDate.get(listDateIndex)+" "+listTime.get(listTimeIndex), Toast.LENGTH_LONG).show();
                            ID = data.getInt(0);
                            break;
                        }
                        Toast.makeText(MainActivity.this, title + "inside while" + date + " " + time +"list", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                title = data.getString(1);
                time = data.getString(3);
                ID = data.getInt(0);
                    //Toast.makeText(MainActivity.this, title + " " + date + " " + time, Toast.LENGTH_LONG).show();
                break;
            }
            date = 0;
        }
    }
}

