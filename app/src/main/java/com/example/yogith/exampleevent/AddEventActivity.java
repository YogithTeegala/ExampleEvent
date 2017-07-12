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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {
    public TextView date, time;
    public EditText editTitle;
    public Button save;
    public String inputDate;
    DBHelper dBHelper;
    private DatePickerDialog.OnDateSetListener onDateSet;
    private TimePickerDialog.OnTimeSetListener onTimeSet;
    String amOrPm;
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
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        onDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                inputDate = day + "/" + month + "/" + year;
                date.setText(inputDate);
            }
        };
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);
                int am_pm=cal.get(Calendar.AM_PM);
                amOrPm = ((am_pm==Calendar.AM)?"am":"pm");
                TimePickerDialog dialog = new TimePickerDialog(AddEventActivity.this, onTimeSet, hour, min, false);
                dialog.show();
            }
        });
        onTimeSet = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String inputTime = hourOfDay + ":" + minute + " " + amOrPm;
                time.setText(inputTime);
            }
        };
    }
    public void addData(View view){
        if(dBHelper.insertData(editTitle.getText().toString(), date.getText().toString(),
                time.getText().toString()))
            Toast.makeText(AddEventActivity.this, editTitle.getText().toString()+" event saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(AddEventActivity.this, "Event not saved", Toast.LENGTH_LONG).show();
        Intent editIntent = new Intent(AddEventActivity.this, MainActivity.class);
        startActivity(editIntent);
    }
}
