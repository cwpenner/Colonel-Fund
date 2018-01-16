package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DonateToEventActivity extends BraintreeActivity {

    public EditText eventDonationTextField;
    public Button eventDonateButton;
    public TextView eventPaymentDescriptionLabel;
    public Button eventSelectPaymentButton;
    public ImageView eventPaymentIconView;

    /**
     * Sets event information.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_donate_to_event);

        eventDonationTextField = findViewById(R.id.eventDonationAmount);
        eventDonateButton = findViewById(R.id.eventDonateButton);
        eventPaymentDescriptionLabel = findViewById(R.id.eventPaymentMethodDescription);
        eventSelectPaymentButton = findViewById(R.id.eventSelectPaymentMethodButton);
        eventPaymentIconView = findViewById(R.id.eventPaymentMethodImage);

        BraintreeActivityInitializer(eventDonationTextField, eventDonateButton, eventPaymentDescriptionLabel, eventSelectPaymentButton, eventPaymentIconView);

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final Event selectedEvent =  (Event) intent.getSerializableExtra("SelectedEvent");

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

        super.setEventTitle(selectedEvent.getTitle());
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

    public void eventSelectPaymentMethodButtonPressed(View v) {
        super.selectPaymentButtonPressed();
    }

    public void eventDonateButtonPressed(View v) {
        super.donateButtonPressed();
    }
}
