package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        lv = (ListView) findViewById(R.id.eventListView);

        //dummy array
        List<String> eventList = new ArrayList<>();
        eventList.add("Independence Day BBQ");
        eventList.add("John Smith Chemo Fund");
        eventList.add("Let's Beat Alzheimer's!");
        eventList.add("Paul's MS Donations");
        eventList.add("Mrs. Cobain Widow Help");

        //make array adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                eventList );

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(EventListActivity.this, ViewEventActivity.class);
                startActivity(intent);
            }
        });
    }
}
