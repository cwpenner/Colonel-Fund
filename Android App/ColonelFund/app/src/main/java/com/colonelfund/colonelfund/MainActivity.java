package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;


// TODO: 12/22/2017 Tie in "Logout" button to terminate users session
public class MainActivity extends AppCompatActivity {
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(this.getApplicationContext());


        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken token = AccessToken.getCurrentAccessToken();
                if(token != null) {
                    LoginManager.getInstance().logOut();
                }
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
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
            Intent intent = new Intent(this, ViewProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param view
     */
    public void viewMemberList(View view) {
        Intent intent = new Intent(this, MemberListActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param view
     */
    public void viewEventList(View view) {
        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param view
     */
    public void createEvent(View view) {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param view
     */
    public void viewProfile(View view) {
        Intent intent = new Intent(this, ViewProfileActivity.class);
        startActivity(intent);
    }
}
