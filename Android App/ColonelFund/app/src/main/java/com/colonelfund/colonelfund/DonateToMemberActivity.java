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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity for donating to a member.
 */
public class DonateToMemberActivity extends BraintreeActivity {
    private GoogleApiClient mGoogleApiClient; // need to implement
    public EditText memberDonationTextField;
    public Button memberDonateButton;
    public TextView memberPaymentDescriptionLabel;
    public Button memberSelectPaymentButton;
    public ImageView memberPaymentIconView;
    Member selectedMember;

    /**
     * Sets Member Information
     * @param savedInstanceState for activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_donate_to_member);

        memberDonationTextField = findViewById(R.id.memberDonationAmount);
        memberDonateButton = findViewById(R.id.memberDonateButton);
        memberPaymentDescriptionLabel = findViewById(R.id.memberPaymentMethodDescription);
        memberSelectPaymentButton = findViewById(R.id.memberSelectPaymentMethodButton);
        memberPaymentIconView = findViewById(R.id.memberPaymentMethodImage);
        BraintreeActivityInitializer(memberDonationTextField, memberDonateButton, memberPaymentDescriptionLabel, memberSelectPaymentButton, memberPaymentIconView);
        //must be called after brain tree initializer or will call braintree null.
        super.onCreate(savedInstanceState);

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
     * @param requestCode for request.
     * @param resultCode for request.
     * @param data for request.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Inflates the menu in top right.
     *
     * @param menu for options in top right.
     * @return boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Selection from detection of menu option selected.
     *
     * @param item selected item.
     * @return boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_item) {
            AccessToken token = AccessToken.getCurrentAccessToken();
            FirebaseAuth.getInstance().signOut();
            //check for google connection (remove once implemented globally)
            if (mGoogleApiClient != null) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
            if(token != null) {
                LoginManager.getInstance().logOut();
            }
            User.logout();
            Intent loginIntent = new Intent(DonateToMemberActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Detected member selected payment button.
     *
     * @param v for view.
     */
    public void memberSelectPaymentMethodButtonPressed(View v) {
        super.selectPaymentButtonPressed();
    }

    /**
     * Detected member donate button pressed.
     *
     * @param v for view.
     */
    public void memberDonateButtonPressed(View v) {
        super.donateButtonPressed();
    }
}
