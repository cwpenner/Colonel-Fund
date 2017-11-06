package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MemberListActivity extends AppCompatActivity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        lv = (ListView) findViewById(R.id.memberListView);

        //dummy array
        List<String> memberList = new ArrayList<>();
        memberList.add("John Smith");
        memberList.add("Liam Gallagher");
        memberList.add("Noel Gallagher");
        memberList.add("John Lennon");
        memberList.add("Paul McCartney");
        memberList.add("Don Henley");
        memberList.add("Phil Collins");
        memberList.add("Jimmy Paige");
        memberList.add("Trevor Hurst");
        memberList.add("Adam Levine");
        memberList.add("Axl Rose");
        memberList.add("Chad Kroeger");
        memberList.add("Dave Grohl");
        memberList.add("Jimi Hendrix");
        memberList.add("Kurt Cobain");

        //make array adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                memberList );

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(MemberListActivity.this, ViewMemberActivity.class);
                startActivity(intent);
            }
        });
    }
}
