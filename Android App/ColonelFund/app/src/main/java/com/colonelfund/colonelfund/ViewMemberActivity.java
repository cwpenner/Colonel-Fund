package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewMemberActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Member selectedMember =  (Member) intent.getSerializableExtra("SelectedMember");
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

        /**
         * Event Info load
         */
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
                Intent intent = new Intent(ViewMemberActivity.this, ViewEventActivity.class);
                startActivity(intent);
            }
        });
    }

    public void donate(View view) {
        Intent intent = new Intent(this, DonateToMemberActivity.class);
        startActivity(intent);
    }
}
