package com.colonelfund.colonelfund;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

// TODO: 12/22/2017 Tie in "Logout" button to terminate users session
public class MainActivity extends AppCompatActivity {
    private GoogleApiClient mGoogleApiClient;
    private DrawerLayout mDrawerLayout;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //ActionBar actionbar = getSupportActionBar();
        //actionbar.setDisplayHomeAsUpEnabled(true);
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment newFragment = new EventListActivity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.viewer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        updateLocalStorage();

        //set listeners for slide out
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        //menuItem.setChecked(true); //removed for now until we fragment code.
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        int id = menuItem.getItemId();
                        if (id == R.id.nav_account) {
                            Intent intent = new Intent(MainActivity.this, ViewProfileActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_history) {
                            Intent intent = new Intent(MainActivity.this, MyHistoryEventsActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_logout) {
                            AccessToken token = AccessToken.getCurrentAccessToken();
                            FirebaseAuth.getInstance().signOut();
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(@NonNull Status status) {
                                            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                            if(token != null) {
                                LoginManager.getInstance().logOut();
                            }
                            User.logout();
                            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                        } else if (id == R.id.nav_members) {
                            Intent intent = new Intent(MainActivity.this, MemberListActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_events) {
                            Intent intent = new Intent(MainActivity.this, EventListActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_create_event) {
                            Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                            startActivity(intent);
                        } else if (id == R.id.nav_main) {
                            //Intent intent = new Intent(AcotvityName.this, MainActivity.this);
                            //startActivity(intent);
                            //removed for main screen for now.
                        }
                        return MainActivity.this.onOptionsItemSelected(menuItem);

                        //return true;
                    }
                });

        //Logout Button code here must be copied to each Activity, as the menu in the top right contains a logout button
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken token = AccessToken.getCurrentAccessToken();
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                if(token != null) {
                    LoginManager.getInstance().logOut();
                }
                User.logout();
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
            Intent intent = new Intent(this, MyHistoryEventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout_item) {
            AccessToken token = AccessToken.getCurrentAccessToken();
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            if(token != null) {
                LoginManager.getInstance().logOut();
            }
            User.logout();
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    /**
     * update local storage from remote Database
     */
    public void updateLocalStorage() {
        MemberCollection mc = new MemberCollection(getApplicationContext());
        mc.updateFromRemote();

        EventCollection ec = new EventCollection(getApplicationContext());
        ec.updateFromRemote();
    }


    /**
     * @param view
     */
    public void viewMemberList(View view) {
        Intent intent = new Intent(this, MemberListActivity.class);
        startActivity(intent);
    }

    /**
     * @param view
     */
    public void viewEventList(View view) {
        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
    }

    /**
     * @param view
     */
    public void createEvent(View view) {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }
}
