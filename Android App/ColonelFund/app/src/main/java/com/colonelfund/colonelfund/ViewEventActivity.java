package com.colonelfund.colonelfund;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewEventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final Event selectedEvent =  (Event) intent.getSerializableExtra("SelectedEvent");
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
}
