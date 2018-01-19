package com.colonelfund.colonelfund;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ViewProfileActivity extends AppCompatActivity {

    private ViewGroup aboutYouLayout;
    private ViewGroup donationInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        aboutYouLayout = (ViewGroup) ViewProfileActivity.this.findViewById(R.id.about_you_table);

        addUserInfoLine("Name:", "Admin Smith");
        addBorder(aboutYouLayout);
        addUserInfoLine("User ID:", "AdminSmith");
        addBorder(aboutYouLayout);
        addUserInfoLine("Email Address:", "Admin@colnelfund.com");

        donationInfoLayout = (ViewGroup) ViewProfileActivity.this.findViewById(R.id.history_table);
        addDonationInfoLine("Donation History Here", "  ");
    }

    private void addUserInfoLine(String leftText, String rightText) {
        View layout2 = LayoutInflater.from(this).inflate(R.layout.about_you_list_item, aboutYouLayout, false);

        TextView textViewLeft = (TextView) layout2.findViewById(R.id.text_left);
        TextView textView1Right = (TextView) layout2.findViewById(R.id.text_right);

        textViewLeft.setText(leftText);
        textView1Right.setText(rightText);

        aboutYouLayout.addView(layout2);
    }

    private void addDonationInfoLine(String leftText, String rightText) {
        View layout3 = LayoutInflater.from(this).inflate(R.layout.about_you_list_item, donationInfoLayout, false);

        TextView textViewLeft = (TextView) layout3.findViewById(R.id.text_left);
        TextView textView1Right = (TextView) layout3.findViewById(R.id.text_right);

        textViewLeft.setText(leftText);
        textView1Right.setText(rightText);

        donationInfoLayout.addView(layout3);
    }

    private void addBorder(ViewGroup viewToAdd) {
        View tableBorder = LayoutInflater.from(this).inflate(R.layout.table_separator, viewToAdd, false);
        viewToAdd.addView(tableBorder);
    }
}
