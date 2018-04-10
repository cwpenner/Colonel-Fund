package com.colonelfund.colonelfund;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ViewProfileFragment extends Fragment implements ImageDownloader.ImageDownloadDelegate {

    private ViewGroup aboutYouLayout;
    private ImageView profilePicImage;
    Context ctx;
    View profileAcivityView;
    private static final String TAG = "ViewProfileFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle("My Account");
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setContentView(R.layout.fragment_view_profile);
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
        if (bitmap != null)
            profilePicImage.setImageBitmap(bitmap);
    }
}
