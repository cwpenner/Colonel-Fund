package com.colonelfund.colonelfund;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;


public class ViewProfileActivity extends AppCompatActivity implements ImageDownloader.ImageDownloadDelegate {

    private ViewGroup aboutYouLayout;
    private ImageView profilePicImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        aboutYouLayout = (ViewGroup) ViewProfileActivity.this.findViewById(R.id.about_you_table);

        addUserInfoLine("Name:", User.currentUser.getFormattedFullName());
        addBorder(aboutYouLayout);
        addUserInfoLine("User ID:", User.currentUser.getUserID());
        addBorder(aboutYouLayout);
        addUserInfoLine("Email Address:", User.currentUser.getEmailAddress());
        addBorder(aboutYouLayout);
        addUserInfoLine("Phone Number:", User.currentUser.getPhoneNumber());

        profilePicImage = findViewById(R.id.profilePicImageView);

        ImageDownloader imageDownloader = new ImageDownloader(this);
        imageDownloader.execute(User.currentUser.getProfilePicURL());

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

    private void addUserInfoLine(String leftText, String rightText) {
        View layout2 = LayoutInflater.from(this).inflate(R.layout.about_you_list_item, aboutYouLayout, false);

        TextView textViewLeft = (TextView) layout2.findViewById(R.id.text_left);
        TextView textView1Right = (TextView) layout2.findViewById(R.id.text_right);

        textViewLeft.setText(leftText);
        textView1Right.setText(rightText);

        aboutYouLayout.addView(layout2);
    }

    private void addBorder(ViewGroup viewToAdd) {
        View tableBorder = LayoutInflater.from(this).inflate(R.layout.table_separator, viewToAdd, false);
        viewToAdd.addView(tableBorder);
    }

    @Override
    public void imageDownloaded(Bitmap bitmap) {
        profilePicImage.setImageBitmap(bitmap);
    }
}
