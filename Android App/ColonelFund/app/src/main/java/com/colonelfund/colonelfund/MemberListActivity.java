package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        final MemberCollection mcf = new MemberCollection(getApplicationContext());
        Collection<Member> memberList = mcf.getMembersList();

        //make array adapter
        ArrayAdapter arrayAdapter = new MemberListAdapter(this, generateData(memberList));
        lv.setAdapter(arrayAdapter);

        // Add listeners for each list item.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemberListModel item = (MemberListModel) lv.getItemAtPosition(position);
                String myItem = item.getUserID();
                Intent intent = new Intent(MemberListActivity.this, ViewMemberActivity.class);
                intent.putExtra("SelectedMember", mcf.get(myItem));
                startActivity(intent);
            }
        });
    }

    /**
     * Added for back button pre API 16
     *
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

    /**
     * Generates Initials and User Name for memberlist.
     *
     * @param memberList
     * @return
     */
    private ArrayList<MemberListModel> generateData(Collection memberList) {
        ArrayList<MemberListModel> models = new ArrayList<MemberListModel>();
        Iterator<Member> memberIter = memberList.iterator();
        while (memberIter.hasNext()) {
            Member temp = memberIter.next();
            String firstName = temp.getFirstName();
            String lastName = temp.getLastName();
            models.add(new MemberListModel(firstName.substring(0, 1) + lastName.substring(0, 1),
                    temp.getUserID(), firstName, lastName));
        }
        return models;
    }
}
