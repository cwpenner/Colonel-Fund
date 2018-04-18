package com.colonelfund.colonelfund;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Fragment that allows a user to view their profile. Instantiated in Main Activity.
 */
public class ViewProfileFragment extends Fragment implements ImageDownloader.ImageDownloadDelegate {

    private ViewGroup aboutYouLayout;
    private ImageView profilePicImage;
    Context ctx;
    View profileActivityView;
    private static final String TAG = "ViewProfileFragment";

    /**
     * Overrides on create to initialize variables for page.
     *
     * @param inflater inflater for fragment views
     * @param container view group for fragment.
     * @param savedInstanceState saved state of fragment.
     * @return view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle("My Account");
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    /**
     * overrides on-create for fragment to initialize values for view.
     *
     * @param savedInstanceState saved state of fragment.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ctx = getActivity();
        profileActivityView = getView();

        aboutYouLayout = (ViewGroup) profileActivityView.findViewById(R.id.about_you_table);

        addUserInfoLine("UserName:", User.currentUser.getUsername());
        addBorder(aboutYouLayout);
        addUserInfoLine("Name:", User.currentUser.getFormattedFullName());
        addBorder(aboutYouLayout);
        addUserInfoLine("User ID:", User.currentUser.getUserID());
        addBorder(aboutYouLayout);
        addUserInfoLine("Email Address:", User.currentUser.getEmailAddress());
        addBorder(aboutYouLayout);
        addUserInfoLine("Phone Number:", User.currentUser.getPhoneNumber());

        profilePicImage = profileActivityView.findViewById(R.id.profilePicImageView);

        ImageDownloader imageDownloader = new ImageDownloader(this);
        if (!User.currentUser.getProfilePicURL().equals("")) {
            imageDownloader.execute(User.currentUser.getProfilePicURL());
        }
    }

    /**
     * Sets the test for user information table.
     *
     * @param leftText text for left side of the table.
     * @param rightText text for thr right side of the table.
     */
    private void addUserInfoLine(String leftText, String rightText) {
        View layout2 = LayoutInflater.from(getActivity()).inflate(R.layout.about_you_list_item, aboutYouLayout, false);

        TextView textViewLeft = (TextView) layout2.findViewById(R.id.text_left);
        TextView textView1Right = (TextView) layout2.findViewById(R.id.text_right);
        textViewLeft.setText(leftText);
        textView1Right.setText(rightText);
        aboutYouLayout.addView(layout2);
    }

    /**
     * Sets a table boarder and separator for table view.
     *
     * @param viewToAdd view group for table layout.
     */
    private void addBorder(ViewGroup viewToAdd) {
        View tableBorder = LayoutInflater.from(getActivity()).inflate(R.layout.table_separator, viewToAdd, false);
        viewToAdd.addView(tableBorder);
    }

    /**
     * Sets profile image.
     *
     * @param bitmap image for profile.
     */
    @Override
    public void imageDownloaded(Bitmap bitmap) {
        if (bitmap != null)
            profilePicImage.setImageBitmap(bitmap);
    }
}
