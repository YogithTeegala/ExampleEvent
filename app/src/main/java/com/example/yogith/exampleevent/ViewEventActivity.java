package com.example.yogith.exampleevent;

import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewEventActivity extends AppCompatActivity {
    DBHelper dBHelper;
    String title, date, time, eTitle;
    private ListView listViewEvent;
    int ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        listViewEvent = (ListView) findViewById(R.id.listView);
        dBHelper = new DBHelper(this);
        displayList();
    }
    private void displayList(){
        Cursor data = dBHelper.fetchData();
        ArrayList<String> listData = new ArrayList();
        while(data.moveToNext()){
            eTitle=data.getString(1);
            listData.add(eTitle);
            //listData.add(data.getString(2));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listViewEvent.setAdapter(adapter);

        listViewEvent.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String object = parent.getItemAtPosition(position).toString();
                Cursor data = dBHelper.getItemID(object);
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Intent editIntent = new Intent(ViewEventActivity.this, EditEventActivity.class);
                    Cursor ata = dBHelper.fetchData();
                    while(ata.moveToNext()) {
                        ID = ata.getInt(0);
                        if(itemID==ID)
                            break;
                    }
                    title = ata.getString(1);
                    date = ata.getString(2);
                    time = ata.getString(3);

                    editIntent.putExtra("id", itemID);
                    editIntent.putExtra("title", title);
                    editIntent.putExtra("date", date);
                    editIntent.putExtra("time", time);
                    startActivity(editIntent);
                }
                else
                    Toast.makeText(ViewEventActivity.this, "No ID", Toast.LENGTH_LONG).show();
            }

        });
    }
}
