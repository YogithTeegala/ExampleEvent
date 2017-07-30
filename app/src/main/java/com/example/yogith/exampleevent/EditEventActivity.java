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

    public String formattedDate, formattedDay, formattedMonth, formattedReceivedTime, formattedReceivedHour, formattedReceivedMin, formattedTime, formattedHour, formattedMinute, selectedTitle, selectedTime, selectedDate, inputTime;
    public int inputDate, selectedID, receivedDate, receivedHour, receivedMin, receivedTime;
    DBHelper dBHelper;
    private DatePickerDialog.OnDateSetListener onDateSet;
    private TimePickerDialog.OnTimeSetListener onTimeSet;

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
        receivedDate = receivedIntent.getIntExtra("date", -1);

        selectedDate = String.valueOf(receivedDate).substring(6, 8)+"/"+String.valueOf(receivedDate).substring(4, 6)+"/"+String.valueOf(receivedDate).substring(0, 4);

        receivedTime = Integer.valueOf(receivedIntent.getStringExtra("time"));
        formattedReceivedTime = String.format("%04d", receivedTime);
        receivedHour = Integer.valueOf(formattedReceivedTime.substring(0, 2));
        receivedMin =  Integer.valueOf(formattedReceivedTime.substring(2, 4));
        Toast.makeText(EditEventActivity.this,""+ formattedReceivedTime+" "+receivedDate, Toast.LENGTH_LONG).show();
        formattedReceivedHour = String.format("%02d", receivedHour);
        formattedReceivedMin = String.format("%02d", receivedMin);
        if(receivedHour>12) {
            receivedHour = receivedHour-12;
            formattedReceivedHour = String.format("%02d", receivedHour);
            selectedTime = formattedReceivedHour + ":"+formattedReceivedMin+" p.m.";
        } else if(receivedHour==12) {
            selectedTime = "12"+ ":"+formattedReceivedMin+" p.m.";
        } else if(receivedHour<12) {
            if(receivedHour!=0) {
                selectedTime = formattedReceivedHour + ":" + formattedReceivedMin + " a.m.";
            } else {
                selectedTime = "12" + ":" + formattedReceivedMin + " a.m.";
            }
        }

        title.setText(selectedTitle);
        date.setText(selectedDate);
        time.setText(selectedTime);

        //inputTitle = selectedTitle;
        inputDate = receivedDate;
        inputTime = String.format("%04d", receivedTime);

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
                TimePickerDialog dialog = new TimePickerDialog(EditEventActivity.this, onTimeSet, hour, min, false);
                dialog.show();

            }
        });
        onTimeSet = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                formattedHour = String.format("%02d", hourOfDay);
                formattedMinute = String.format("%02d", minute);
                inputTime = formattedHour + formattedMinute;
                if(hourOfDay>12) {
                    int hour = hourOfDay-12;
                    formattedHour = String.format("%02d", hour);
                    formattedTime = formattedHour + ":"+formattedMinute+" p.m.";
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
        dBHelper.deleteData(selectedID);
        Toast.makeText(EditEventActivity.this, selectedTitle+" deleted", Toast.LENGTH_LONG).show();
        Intent editIntent = new Intent(EditEventActivity.this, ViewEventActivity.class);
        startActivity(editIntent);
    }
}
