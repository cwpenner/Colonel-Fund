package com.colonelfund.colonelfund;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

/**
 * Activity for donating to a member.
 */
public class DonateToMemberActivity extends BraintreeActivity {

    public EditText memberDonationTextField;
    public Button memberDonateButton;
    public TextView memberPaymentDescriptionLabel;
    public Button memberSelectPaymentButton;
    public ImageView memberPaymentIconView;
    Member selectedMember;

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
        selectedMember =  (Member) intent.getSerializableExtra("SelectedMember");

        TextView fullNameText = (TextView) findViewById(R.id.donateViewMemberName);
        fullNameText.setText(selectedMember.getFormattedFullName());

        TextView emailText = (TextView) findViewById(R.id.donateViewMemberEmail);
        emailText.setText(selectedMember.getEmailAddress());

        super.setMemberName(selectedMember.getFirstName() + " " + selectedMember.getLastName());
    }

    /**
     * For back button at top left of screen, pass back intent params
     * https://developer.android.com/training/basics/intents/result.html
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
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

    public void memberSelectPaymentMethodButtonPressed(View v) {
        super.selectPaymentButtonPressed();
    }

    public void memberDonateButtonPressed(View v) {
        super.donateButtonPressed();
    }
}
