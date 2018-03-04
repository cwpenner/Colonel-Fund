package com.colonelfund.colonelfund;

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

public class MyHistoryEventsActivity extends AppCompatActivity {

    private ViewGroup donationInfoLayout;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history_events);

        donationInfoLayout = (ViewGroup) MyHistoryEventsActivity.this.findViewById(R.id.history_table);
        addDonationInfoLine("Donation History Here", "  ");
        addBorder(donationInfoLayout);
        addDonationInfoLine("Example Event Title", "$15.02");
        addBorder(donationInfoLayout);
        addDonationInfoLine("Example Event Title 2", "$1.02");

        /**
         * Event Info load
         */
        Member aMember;
        aMember = new Member("93471", "Test", "Event", "test@gmail.com", "987-654-3210");
        final Member selectedMember = aMember;
        lv = (ListView) findViewById(R.id.associated_events_table);

        //event array
        final EventCollection ecf = new EventCollection(getApplicationContext());
        ArrayList<String> eventList = ecf.getAssociatedEvents(selectedMember.getUserID());

        //make array adapter
        if (eventList != null && !eventList.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, eventList);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = lv.getItemAtPosition(position);
                    String myItem = item.toString();
                    Intent intent = new Intent(MyHistoryEventsActivity.this, ViewEventActivity.class);
                    intent.putExtra("SelectedEvent", ecf.get(myItem));
                    startActivity(intent);
                }
            });
        } else {
            eventList.add("You have no associated events.");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    eventList);
            lv.setAdapter(arrayAdapter);
            lv.setEnabled(false);
        }
    }

    private void addDonationInfoLine(String leftText, String rightText) {
        View layout3 = LayoutInflater.from(this).inflate(R.layout.about_you_list_item, donationInfoLayout, false);

        TextView textViewLeft = (TextView) layout3.findViewById(R.id.text_left);
        TextView textView1Right = (TextView) layout3.findViewById(R.id.text_right);

        textViewLeft.setText(leftText);
        textView1Right.setText(rightText);

        donationInfoLayout.addView(layout3);
    }

    private void addBorder(ViewGroup viewToAdd) {
        View tableBorder = LayoutInflater.from(this).inflate(R.layout.table_separator, viewToAdd, false);
        viewToAdd.addView(tableBorder);
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
            if (token != null) {
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
}
