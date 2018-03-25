package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;


public class ViewProfileActivity extends Fragment implements ImageDownloader.ImageDownloadDelegate {

    private ViewGroup aboutYouLayout;
    private ImageView profilePicImage;
    Context ctx;
    View profileAcivityView;
    private static final String TAG = "ViewProfileActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_view_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setContentView(R.layout.activity_view_profile);
        ctx = getActivity();
        profileAcivityView = getView();

        aboutYouLayout = (ViewGroup) profileAcivityView.findViewById(R.id.about_you_table);

        addUserInfoLine("Name:", User.currentUser.getFormattedFullName());
        addBorder(aboutYouLayout);
        addUserInfoLine("User ID:", User.currentUser.getUserID());
        addBorder(aboutYouLayout);
        addUserInfoLine("Email Address:", User.currentUser.getEmailAddress());
        addBorder(aboutYouLayout);
        addUserInfoLine("Phone Number:", User.currentUser.getPhoneNumber());

        profilePicImage = profileAcivityView.findViewById(R.id.profilePicImageView);

        ImageDownloader imageDownloader = new ImageDownloader(this);
        imageDownloader.execute(User.currentUser.getProfilePicURL());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_you) {
            Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.your_history_events) {
            Intent intent = new Intent(getActivity(), MyHistoryEventsActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout_item) {
            AccessToken token = AccessToken.getCurrentAccessToken();
            //TODO: Add Google logout code
            if(token != null) {
                LoginManager.getInstance().logOut();
            }
            User.logout();
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
        } else if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addUserInfoLine(String leftText, String rightText) {
        View layout2 = LayoutInflater.from(getActivity()).inflate(R.layout.about_you_list_item, aboutYouLayout, false);

        TextView textViewLeft = (TextView) layout2.findViewById(R.id.text_left);
        TextView textView1Right = (TextView) layout2.findViewById(R.id.text_right);

        textViewLeft.setText(leftText);
        textView1Right.setText(rightText);

        aboutYouLayout.addView(layout2);
    }

    private void addBorder(ViewGroup viewToAdd) {
        View tableBorder = LayoutInflater.from(getActivity()).inflate(R.layout.table_separator, viewToAdd, false);
        viewToAdd.addView(tableBorder);
    }

    @Override
    public void imageDownloaded(Bitmap bitmap) {
        profilePicImage.setImageBitmap(bitmap);
    }
}
