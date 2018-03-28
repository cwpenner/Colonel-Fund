package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
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

    public void eventSelectPaymentMethodButtonPressed(View v) {
        super.selectPaymentButtonPressed();
    }

    public void eventDonateButtonPressed(View v) {
        super.donateButtonPressed();
    }
}
