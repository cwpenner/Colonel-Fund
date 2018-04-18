package com.colonelfund.colonelfund;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

/**
 * Main activity that holds fragments for all main activities and a nav drawer.
 */
// TODO: 12/22/2017 Tie in "Logout" button to terminate users session
public class MainActivity extends AppCompatActivity implements ImageDownloader.ImageDownloadDelegate {
    private GoogleApiClient mGoogleApiClient;
    private DrawerLayout mDrawerLayout;
    Fragment newFragment;
    private NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private ImageView profilePicImage;

    /**
     * Overrides on create to set variables to proper values.
     *
     * @param savedInstanceState of activity.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        updateLocalStorage();

        //set listeners for slide out
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fragmentManager = getSupportFragmentManager();
        newFragment = new MainPageFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewer, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        // Selection of UI fragment here
                        int id = menuItem.getItemId();
                        if (id == R.id.nav_account) {
                            newFragment = new ViewProfileFragment();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.viewer, newFragment);
                            fragmentTransaction.addToBackStack(null);
                        } else if (id == R.id.nav_history) {
                            newFragment = new MyHistoryEventsFragment();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.viewer, newFragment);
                            fragmentTransaction.addToBackStack(null);
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
                            return true;
                        } else if (id == R.id.nav_members) {
                            newFragment = new MemberListFragment();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.viewer, newFragment);
                            fragmentTransaction.addToBackStack(null);
                        } else if (id == R.id.nav_events) {
                            newFragment = new EventListFragment();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.viewer, newFragment);
                            fragmentTransaction.addToBackStack(null);
                        } else if (id == R.id.nav_create_event) {
                            newFragment = new CreateEventFragment();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.viewer, newFragment);
                            fragmentTransaction.addToBackStack(null);
                        } else if (id == R.id.nav_main) {
                            newFragment = new MainPageFragment();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.viewer, newFragment);
                            fragmentTransaction.addToBackStack(null);
                        }
                        fragmentTransaction.commit();
                        return true;
                    }
                });
        this.getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        Fragment current = getCurrentFragment();
                            Fragment f = getSupportFragmentManager().findFragmentById(R.id.nav_view);
                            if (current instanceof MemberListFragment) {
                                navigationView.setCheckedItem(R.id.nav_members);
                                System.out.println ("Nav members Back Detect");
                            } else if (current instanceof CreateEventFragment) {
                                navigationView.setCheckedItem(R.id.nav_create_event);
                                System.out.println ("Nav create Back Detect");
                            } else if (current instanceof EventListFragment) {
                                navigationView.setCheckedItem(R.id.nav_events);
                                System.out.println ("Nav events Back Detect");
                            } else if (current instanceof MyHistoryEventsFragment) {
                                navigationView.setCheckedItem(R.id.nav_history);
                                System.out.println ("Nav history Back Detect");
                            } else if (current instanceof ViewProfileFragment) {
                                navigationView.setCheckedItem(R.id.nav_account);
                                System.out.println ("Nav Account Back Detect");
                            } else if (current instanceof MainPageFragment) {
                                navigationView.setCheckedItem(R.id.nav_main);
                                System.out.println ("Nav Main Back Detect");
                            }
                    }
                });

        //fill in user info in nav header
        View navHeader = navigationView.getHeaderView(0);
        profilePicImage = navHeader.findViewById(R.id.nav_profilePicture);
        ImageDownloader imageDownloader = new ImageDownloader(this);
        if (!User.currentUser.getProfilePicURL().equals("")) {
            imageDownloader.execute(User.currentUser.getProfilePicURL());
        }
        TextView navUserName = navHeader.findViewById(R.id.nav_userName);
        navUserName.setText(User.getCurrentUser().getFormattedFullName());
        TextView navUserEmail = navHeader.findViewById(R.id.nav_userEmail);
        navUserEmail.setText(User.getCurrentUser().getEmailAddress());
    }

    /**
     * Fragment handler.
     *
     * @return current fragment.
     */
    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.viewer);
    }

    /**
     * OnStart override for google authentication
     */
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
     * Overrides on back pressed for fragment navigation.
     */
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * Sets profile picture to users value.
     *
     * @param bitmap of user profile picture
     */
    @Override
    public void imageDownloaded(Bitmap bitmap) {
        if (bitmap != null)
            profilePicImage.setImageBitmap(bitmap);
    }
}
