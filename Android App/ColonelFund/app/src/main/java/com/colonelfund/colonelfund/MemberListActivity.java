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
        ArrayAdapter arrayAdapter = new MyMemberAdapter(
                this, generateData(memberList) );

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                MemberModel item = (MemberModel) lv.getItemAtPosition(position);
                String myItem = item.getMemberID();
                Intent intent = new Intent(MemberListActivity.this, ViewMemberActivity.class);
                intent.putExtra("SelectedMember", mcf.get(myItem));
                startActivity(intent);
            }
        });
    }

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

    private ArrayList<MemberModel> generateData(Collection memberList) {
        ArrayList<MemberModel> models = new ArrayList<MemberModel>();
        Iterator<Member> memberIter = memberList.iterator();
        while (memberIter.hasNext()) {
            Member temp = memberIter.next();
            models.add(new MemberModel(temp.getFirstName().substring(0,1)+temp.getLastName().substring(0,1),temp.getUserID()));
        }
        return models;
    }
}
