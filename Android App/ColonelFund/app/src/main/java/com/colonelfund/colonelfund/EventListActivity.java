package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventListActivity extends AppCompatActivity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        lv = (ListView) findViewById(R.id.eventListView);

        //dummy array
        final EventCollection ecf = new EventCollection(getApplicationContext());
        List<String> eventList = new ArrayList<>(Arrays.asList(ecf.getTitles()));

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
                Object item = lv.getItemAtPosition(position);
                String myItem = item.toString();
                Intent intent = new Intent(EventListActivity.this, ViewEventActivity.class);
                intent.putExtra("SelecetedEvent", ecf.get(myItem));
                startActivity(intent);
            }
        });
    }
}
