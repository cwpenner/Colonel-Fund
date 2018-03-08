package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

/**
 * Activity for Member Viewing an Event.
 */
public class ViewEventActivity extends AppCompatActivity {
    /**
     * Paints event info to screen.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Event aEvent;
        if ((Event) intent.getSerializableExtra("SelectedEvent") != null) {
            aEvent = (Event) intent.getSerializableExtra("SelectedEvent");
        } else {
            aEvent = new Event ("Error", "Error", "Error",0.0,0.0, "Error", "Error");
        }
        final Event selectedEvent =  aEvent;
        setContentView(R.layout.activity_view_event);
        TextView text = (TextView) findViewById(R.id.textView3);
        text.setText(selectedEvent.getTitle());
        text = (TextView) findViewById(R.id.textView11);
        text.setText(selectedEvent.getEventDate());
        text = (TextView) findViewById(R.id.textView9);
        text.setText(selectedEvent.getAssociatedEmail());
        text = (TextView) findViewById(R.id.textView5);
        text.setText(String.valueOf(selectedEvent.getFundGoal()));
        text = (TextView) findViewById(R.id.textView7);
        text.setText(String.valueOf(selectedEvent.getCurrentFunds()));
        text = (TextView) findViewById(R.id.textView2);
        text.setText(String.valueOf(selectedEvent.getDescription()));

        Button donateButton = findViewById(R.id.button);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ViewEventActivity.this, DonateToEventActivity.class);
                intent2.putExtra("SelectedEvent", selectedEvent);
                startActivity(intent2);
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
}
