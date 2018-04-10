package com.colonelfund.colonelfund;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

/**
 * Activity for Member Viewing a member.
 */
public class ViewMemberActivity extends AppCompatActivity {

    private ListView lv;

    /**
     * Creates member view and gets/adds related activities.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Member aMember;
        if ((Member) intent.getSerializableExtra("SelectedMember") != null) {
            aMember = (Member) intent.getSerializableExtra("SelectedMember");
        } else {
            aMember = new Member("Error", "Error", "Error", "Error", "Error", "Error", "Error", "Error", "Error", "Error");
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

        // Listener for donating to member.
        Button donateButton = findViewById(R.id.memberDonateButton);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ViewMemberActivity.this, DonateToMemberActivity.class);
                intent2.putExtra("SelectedMember", selectedMember);
                startActivity(intent2);
//                startActivityForResult(intent2,0);
            }
        });
    }

    /**
     * Added for back button pre API 16
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
/*
    /**
     * For back button at top left of screen, pass back intent params
     * https://developer.android.com/training/basics/intents/result.html
     * @param requestCode
     * @param resultCode
     * @param data
     */
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_item) {
            AccessToken token = AccessToken.getCurrentAccessToken();
            //TODO: Add Google logout code
            if(token != null) {
                LoginManager.getInstance().logOut();
            }
            User.logout();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
