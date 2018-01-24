package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Activity for Member list creation.
 */
public class MemberListActivity extends AppCompatActivity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_member_list);

        lv = (ListView) findViewById(R.id.memberListView);

        //dummy array
        final MemberCollection mcf = new MemberCollection(getApplicationContext());
        Collection<Member> memberList = mcf.getMembersList();

        //make array adapter
        ArrayAdapter arrayAdapter = new MemberListAdapter(
                this, generateData(memberList) );

        lv.setAdapter(arrayAdapter);

         // Add listeners for each list item.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                MemberListModel item = (MemberListModel) lv.getItemAtPosition(position);
                String myItem = item.getMemberID();
                Intent intent = new Intent(MemberListActivity.this, ViewMemberActivity.class);
                intent.putExtra("SelectedMember", mcf.get(myItem));
                startActivity(intent);
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
            if(token != null) {
                LoginManager.getInstance().logOut();
            }
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else if (id == R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Generates Initials and User Name for memberlist.
     * @param memberList
     * @return
     */
    private ArrayList<MemberListModel> generateData(Collection memberList) {
        ArrayList<MemberListModel> models = new ArrayList<MemberListModel>();
        Iterator<Member> memberIter = memberList.iterator();
        while (memberIter.hasNext()) {
            Member temp = memberIter.next();
            models.add(new MemberListModel(temp.getFirstName().substring(0,1)+temp.getLastName().substring(0,1),temp.getUserID()));
        }
        return models;
    }
}
