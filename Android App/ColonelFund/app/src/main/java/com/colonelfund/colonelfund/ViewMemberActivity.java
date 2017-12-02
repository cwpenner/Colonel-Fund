package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewMemberActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Member aMember;
        if ((Member) intent.getSerializableExtra("SelectedMember") != null) {
            aMember = (Member) intent.getSerializableExtra("SelectedMember");
        } else {
            aMember = new Member ("Error", "Error", "Error", "Error", "Error", "Error");
        }
        final Member selectedMember = aMember;
        setContentView(R.layout.activity_view_member);

        /**
         * Member info Load
         */
        TextView text = (TextView) findViewById(R.id.textView3);
        text.setText(selectedMember.getUserID());
        text = (TextView) findViewById(R.id.textView12);
        text.setText(selectedMember.getEmailAddress());
        text = (TextView) findViewById(R.id.textView13);
        text.setText(selectedMember.getPhoneNumber());
        text = (TextView) findViewById(R.id.textView16);
        text.setText(selectedMember.getFirstName() + " " + selectedMember.getLastName());
        text = (TextView) findViewById(R.id.textView15);
        text.setText(selectedMember.getFirstName().substring(0, 1) + selectedMember.getLastName().substring(0, 1));

        /**
         * Event Info load
         */
        lv = (ListView) findViewById(R.id.eventListView);

        //event array
        final EventCollection ecf = new EventCollection(getApplicationContext());
        ArrayList<String> eventList = ecf.getAssociatedEvents(selectedMember.getUserID());

        //make array adapter
        if (eventList != null && !eventList.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    eventList);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Object item = lv.getItemAtPosition(position);
                    String myItem = item.toString();
                    Intent intent = new Intent(ViewMemberActivity.this, ViewEventActivity.class);
                    intent.putExtra("SelectedEvent", ecf.get(myItem));
                    startActivity(intent);
                }
            });
        } else {
            eventList.add("This user has no associated events.");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    eventList);
            lv.setAdapter(arrayAdapter);
            lv.setEnabled(false);
        }

        Button donateButton = findViewById(R.id.button4);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ViewMemberActivity.this, DonateToMemberActivity.class);
                intent2.putExtra("SelectedMember", selectedMember);
                startActivity(intent2);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
