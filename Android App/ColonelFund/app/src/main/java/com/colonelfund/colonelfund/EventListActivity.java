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
        EventCollection ec = new EventCollection();
        Event aEvent = new Event("Independance Day BBQ", "JohnSmith", "2018-07-04", 1000.00, 75.63, "Fun BBQ party for 388 Dark Knights!");
        System.out.println(ec.add(aEvent));
        aEvent = new Event("John's Chemo Fund", "JohnSmith", "2018-03-20", 5000.00, 1075.63, "Help me pay my medical bills!");
        System.out.println(ec.add(aEvent));
        aEvent = new Event("Alzheimer's Research Fund", "LaimGallagher", "2018-01-18", 7550.00, 750.63, "Help out researchers.");
        System.out.println(ec.add(aEvent));
        aEvent = new Event("Paul's MS Donations", "PaulMcCartney", "2018-05-14", 8367.53, 7505.63, "Help me fund my MS bills.");
        System.out.println(ec.add(aEvent));
        aEvent = new Event("Mrs. Cobain's Widow Help", "KimCobain", "2018-02-05", 3500.00, 2735.63, "Help out a Widow.");
        System.out.println(ec.add(aEvent));
        List<String> eventList = new ArrayList<>(Arrays.asList(ec.getTitles()));
        final EventCollection ecf = ec;

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
