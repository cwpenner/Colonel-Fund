package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DonateToEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
