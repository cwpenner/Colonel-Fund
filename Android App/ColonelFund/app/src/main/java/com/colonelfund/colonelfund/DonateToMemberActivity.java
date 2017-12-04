package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Activity for donating to a member.
 */
public class DonateToMemberActivity extends AppCompatActivity {
    /**
     * Sets Member Information
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Member selectedMember =  (Member) intent.getSerializableExtra("SelectedMember");
        setContentView(R.layout.activity_donate_to_member);
        TextView text = (TextView) findViewById(R.id.textView3);
        text.setText(selectedMember.getUserID());
    }
    /**
     * Added for back button pre API 16
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
