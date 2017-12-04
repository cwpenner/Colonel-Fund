package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class DonateToEventActivity extends AppCompatActivity {
    /**
     * Sets event information.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final Event selectedEvent =  (Event) intent.getSerializableExtra("SelectedEvent");

        setContentView(R.layout.activity_donate_to_event);

        TextView text = findViewById(R.id.textView3);
        text.setText(selectedEvent.getTitle());
        text = findViewById(R.id.textView11);
        text.setText(selectedEvent.getEventDate());
        text = findViewById(R.id.textView9);
        text.setText(selectedEvent.getAssociatedMember());
        text = findViewById(R.id.textView5);
        text.setText(String.valueOf(selectedEvent.getFundGoal()));
        text = findViewById(R.id.textView7);
        text.setText(String.valueOf(selectedEvent.getCurrentFunds()));
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
