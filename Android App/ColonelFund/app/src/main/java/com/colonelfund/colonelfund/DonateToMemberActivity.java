package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DonateToMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Member selectedMember =  (Member) intent.getSerializableExtra("SelectedMember");
        setContentView(R.layout.activity_donate_to_member);
        TextView text = (TextView) findViewById(R.id.textView3);
        text.setText(selectedMember.getUserID());
    }
}
