package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
            aEvent = new Event ("Error", "Error", "Error",0.0,0.0, "Error");
        }
        final Event selectedEvent =  aEvent;
        setContentView(R.layout.activity_view_event);
        TextView text = (TextView) findViewById(R.id.textView3);
        text.setText(selectedEvent.getTitle());
        text = (TextView) findViewById(R.id.textView11);
        text.setText(selectedEvent.getEventDate());
        text = (TextView) findViewById(R.id.textView9);
        text.setText(selectedEvent.getAssociatedMember());
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
    /**
     * Added for back button pre API 16
     * @param item
     * @return
     */
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
