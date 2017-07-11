package com.example.yogith.exampleevent;

/**
 * Created by Yogith on 08-07-2017.
 */
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity{

    public Button saveButton, deleteButton;
    public EditText title;
    public TextView date, time;

    public String inputDate, inputTime, inputTitle;
    DBHelper dBHelper;
    private DatePickerDialog.OnDateSetListener onDateSet;
    private TimePickerDialog.OnTimeSetListener onTimeSet;
    String amOrPm;

    public String selectedTitle, selectedDate, selectedTime;
    public int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        title = (EditText) findViewById(R.id.titleEditText) ;
        date = (TextView) findViewById(R.id.dateDisplay);
        time = (TextView) findViewById(R.id.timeDisplay);
        saveButton = (Button) findViewById(R.id.saveButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        dBHelper = new DBHelper(this);

        Intent receivedIntent  = getIntent();
        selectedID = receivedIntent.getIntExtra("id", -1);
        selectedTitle = receivedIntent.getStringExtra("title");
        selectedDate = receivedIntent.getStringExtra("date");
        selectedTime = receivedIntent.getStringExtra("time");
        title.setText(selectedTitle);
        date.setText(selectedDate);
        time.setText(selectedTime);

        //inputTitle = selectedTitle;
        inputDate = selectedDate;
        inputTime = selectedTime;

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(
                        EditEventActivity.this, onDateSet, year, month, dayOfMonth);
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
                TimePickerDialog dialog = new TimePickerDialog(EditEventActivity.this, onTimeSet, hour, min, false);
                dialog.show();
            }
        });
        onTimeSet = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                inputTime = hourOfDay + ":" + minute + " " + amOrPm;
                time.setText(inputTime);
            }
        };

    }
    public void updateEvent(View view){
        String inputTitle = title.getText().toString();
        if(!inputTitle.equals("")){
            dBHelper.updateData( inputTitle, selectedTitle, inputDate, inputTime, selectedID);
            Toast.makeText(EditEventActivity.this, inputTitle+" edited", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(EditEventActivity.this, "Title shouldn't be empty", Toast.LENGTH_LONG).show();
        Intent editIntent = new Intent(EditEventActivity.this, ViewEventActivity.class);
        startActivity(editIntent);
    }
    public void deleteEvent(View view){
        dBHelper.deleteData(selectedID, selectedTitle);
        Toast.makeText(EditEventActivity.this, selectedTitle+" deleted", Toast.LENGTH_LONG).show();
        Intent editIntent = new Intent(EditEventActivity.this, ViewEventActivity.class);
        startActivity(editIntent);
    }
}
