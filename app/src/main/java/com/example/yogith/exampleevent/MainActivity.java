package com.example.yogith.exampleevent;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void addButton(View view) {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }
    public void viewButton(View view) {
        Intent intent = new Intent(this, ViewEventActivity.class);
        startActivity(intent);
    }





}
