package com.colonelfund.colonelfund;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
 * Activity for Member Viewing an Event.
 */
public class ViewEventActivity extends AppCompatActivity implements ImageDownloader.ImageDownloadDelegate {
    private GoogleApiClient mGoogleApiClient; // need to implement
    private ImageView imageView;

    /**
     * Paints event info to screen.
     *
     * @param savedInstanceState of activity.
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
            aEvent = new Event("Error", "Error", "Error", 0.0, 0.0, "Error", "Error");
        }
        final Event selectedEvent = aEvent;
        MemberCollection mc = new MemberCollection(this);
        Member associatedMember = mc.get(selectedEvent.getAssociatedMember());

        setContentView(R.layout.activity_view_event);
        TextView text = (TextView) findViewById(R.id.textView3);
        text.setText(selectedEvent.getTitle());
        text = (TextView) findViewById(R.id.textView11);
        text.setText(selectedEvent.getEventDate());
        text = (TextView) findViewById(R.id.textView9);
        text.setText(associatedMember.getFormattedFullName());
        text = (TextView) findViewById(R.id.textView5);
        text.setText("$" + String.valueOf(selectedEvent.getFundGoal()));
        text = (TextView) findViewById(R.id.textView7);
        text.setText("$" + String.valueOf(selectedEvent.getCurrentFunds()));
        text = (TextView) findViewById(R.id.textView2);
        text.setText(String.valueOf(selectedEvent.getDescription()));

        imageView = (ImageView) findViewById(R.id.viewEventImage);
        ImageDownloader imageDownloader = new ImageDownloader(this);
        imageDownloader.execute(selectedEvent.getImageURL());

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
     * inflates the menu options in top right.
     *
     * @param menu of options.
     * @return boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Action on menu option selected.
     *
     * @param item menu item
     * @return boolean
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
            Intent loginIntent = new Intent(ViewEventActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets user profile picture at finish of download.
     *
     * @param bitmap of user profile picture
     */
    @Override
    public void imageDownloaded(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
