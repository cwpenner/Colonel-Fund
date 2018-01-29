package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.braintreepayments.api.models.BraintreePaymentResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

/**
 * Activity for donating to a member.
 */
public class DonateToMemberActivity extends BraintreeActivity {

    public EditText memberDonationTextField;
    public Button memberDonateButton;
    public TextView memberPaymentDescriptionLabel;
    public Button memberSelectPaymentButton;
    public ImageView memberPaymentIconView;

    /**
     * Sets Member Information
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_donate_to_member);

        memberDonationTextField = findViewById(R.id.memberDonationAmount);
        memberDonateButton = findViewById(R.id.memberDonateButton);
        memberPaymentDescriptionLabel = findViewById(R.id.memberPaymentMethodDescription);
        memberSelectPaymentButton = findViewById(R.id.memberSelectPaymentMethodButton);
        memberPaymentIconView = findViewById(R.id.memberPaymentMethodImage);
        BraintreeActivityInitializer(memberDonationTextField, memberDonateButton, memberPaymentDescriptionLabel, memberSelectPaymentButton, memberPaymentIconView);

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Member selectedMember =  (Member) intent.getSerializableExtra("SelectedMember");

        TextView text = (TextView) findViewById(R.id.textView3);
        text.setText(selectedMember.getUserID());

        super.setMemberName(selectedMember.getFirstName() + " " + selectedMember.getLastName());
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
        } else if (id == R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void memberSelectPaymentMethodButtonPressed(View v) {
        super.selectPaymentButtonPressed();
    }

    public void memberDonateButtonPressed(View v) {
        super.donateButtonPressed();
    }
}
