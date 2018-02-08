package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Event list view class.
 */
public class EventListActivity extends AppCompatActivity {
    private ListView lv;

    /**
     * Draws event list
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_event_list);

        lv = (ListView) findViewById(R.id.eventListView);

        //dummy array
        final EventCollection ecf = new EventCollection(getApplicationContext());
        Collection<Event> eventList = ecf.getEventsList();
        //List<String> eventList = new ArrayList<>(Arrays.asList(ecf.getTitles()));

        //make array adapter
        ArrayAdapter arrayAdapter = new EventListAdapter(this, generateData(eventList));
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
        //        this,
        //        android.R.layout.simple_list_item_1,
        //        eventList );

        lv.setAdapter(arrayAdapter);

        // set listener for each item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                EventListModel item = (EventListModel) lv.getItemAtPosition(position);
                String myItem = item.getTitle();
                Intent intent = new Intent(EventListActivity.this, ViewEventActivity.class);
                intent.putExtra("SelectedEvent", ecf.get(myItem));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_you) {
            Intent intent = new Intent(this, ViewProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.your_history_events) {
            Intent intent = new Intent(this, MyHistoryEventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout_item) {
            AccessToken token = AccessToken.getCurrentAccessToken();
            if(token != null) {
                LoginManager.getInstance().logOut();
            }
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Generates Initials and User Name for memberlist.
     *
     * @param eventList
     * @return
     */
    private ArrayList<EventListModel> generateData(Collection eventList) {
        ArrayList<EventListModel> models = new ArrayList<EventListModel>();
        Iterator<Event> EventItr = eventList.iterator();
        while (EventItr.hasNext()) {
            Event temp = EventItr.next();
            String eventTitle = temp.getTitle();
            //String eventType = temp.getEventType();
            String eventType = "Placeholder";
            models.add(new EventListModel(eventTitle,eventType));
        }
        return models;
    }
}

/**
 * Event list adapter class.
 */
class EventListAdapter extends ArrayAdapter<EventListModel> {

    private final Context context;
    private final ArrayList<EventListModel> modelsArrayList;

    /**
     * Constructor for member list item adapter.
     * @param context
     * @param modelsArrayList
     */
    public EventListAdapter(Context context, ArrayList<EventListModel> modelsArrayList) {
        super(context, R.layout.event_list_item, modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    /**
     * Gets View for Member List Item.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = null;
        rowView = inflater.inflate(R.layout.event_list_item, parent, false);

        //TextView memberInitials = (TextView) rowView.findViewById(R.id.event_type_pic);
        TextView memberName = (TextView) rowView.findViewById(R.id.eventName);

        //memberInitials.setText(modelsArrayList.get(position).getInitials());
        memberName.setText(modelsArrayList.get(position).getTitle());

        return rowView;
    }
}

/**
 * Event list Item Model class.
 */
class EventListModel {
    private String title;
    private String type;

    /**
     * Constructor for Initials circle.
     * @param title
     * @param type
     */
    public EventListModel(String title, String type) {
        super();
        this.title = title;
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public String getTitle() {
        return title;
    }

}

