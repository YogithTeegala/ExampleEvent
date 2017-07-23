package com.example.yogith.exampleevent;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {
    public TextView date, time;
    public EditText editTitle;
    public Button save;
    public String formattedDate, formattedDay, formattedMonth, formattedTime, formattedHour, formattedMinute;
    public int inputDate, inputTime;
    DBHelper dBHelper;
    private DatePickerDialog.OnDateSetListener onDateSet;
    private TimePickerDialog.OnTimeSetListener onTimeSet;

//    dBHelper = new DBHelper(this);
//    SQLiteDatabase db = dBHelper.getWritableDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        editTitle = (EditText) findViewById(R.id.titleEditText) ;
        date = (TextView) findViewById(R.id.dateDisplay);
        time = (TextView) findViewById(R.id.timeDisplay);
        save = (Button) findViewById(R.id.saveButton);
        dBHelper = new DBHelper(this);
        SQLiteDatabase db = dBHelper.getWritableDatabase();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(
                        AddEventActivity.this, onDateSet, year, month, dayOfMonth);
                dialog.show();
            }
        });
        onDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                formattedDay = String.format("%02d", day);
                formattedMonth = String.format("%02d", month);
                inputDate = Integer.parseInt(String.valueOf(year) + formattedMonth + formattedDay);
                formattedDate = formattedDay + "/" + formattedMonth + "/" + year;
                date.setText(formattedDate);
            }
        };
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(AddEventActivity.this, onTimeSet, hour, min, false);
                dialog.show();

            }
        });
        onTimeSet = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(AddEventActivity.this, hourOfDay+" "+minute, Toast.LENGTH_LONG).show();
                formattedHour = String.format("%02d", hourOfDay);
                formattedMinute = String.format("%02d", minute);
                inputTime = Integer.parseInt(formattedHour + formattedMinute);
                if(hourOfDay>12) {
                    int hour = hourOfDay-12;
                    formattedHour = String.format("%02d", hour);
                    formattedTime = formattedHour+ ":"+formattedMinute+" p.m.";
                } else if(hourOfDay==12) {
                    formattedTime = "12"+ ":"+formattedMinute+" p.m.";
                } else if(hourOfDay<12) {
                    if(hourOfDay!=0) {
                        formattedTime = formattedHour + ":" + formattedMinute + " a.m.";
                    } else {
                        formattedTime = "12" + ":" + formattedMinute + " a.m.";
                    }
                }
                time.setText(formattedTime);
            }
        };
    }
    public void addData(View view){
        if(dBHelper.insertData(editTitle.getText().toString(), inputDate,
                inputTime))
            Toast.makeText(AddEventActivity.this, editTitle.getText().toString()+" event saved", Toast.LENGTH_LONG).show();

        else
            Toast.makeText(AddEventActivity.this, "Event not saved", Toast.LENGTH_LONG).show();
        Intent editIntent = new Intent(AddEventActivity.this, MainActivity.class);
        startActivity(editIntent);
    }
}
