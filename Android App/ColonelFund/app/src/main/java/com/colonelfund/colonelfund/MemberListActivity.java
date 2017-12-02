package com.colonelfund.colonelfund;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemberListActivity extends AppCompatActivity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        lv = (ListView) findViewById(R.id.memberListView);

        //dummy array
        final MemberCollection mcf = new MemberCollection(getApplicationContext());
        List<String> memberList = new ArrayList<>(Arrays.asList(mcf.getMembers()));


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
                Object item = lv.getItemAtPosition(position);
                String myItem = item.toString();
                Intent intent = new Intent(MemberListActivity.this, ViewMemberActivity.class);
                intent.putExtra("SelectedMember", mcf.get(myItem));
                startActivity(intent);
            }
        });
    }
}
