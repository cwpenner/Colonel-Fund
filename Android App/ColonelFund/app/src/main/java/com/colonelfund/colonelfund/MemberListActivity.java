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
        MemberCollection mc = new MemberCollection();
        Member aMember = new Member("JohnSmith","John","Smith","JohnSmith@email.com","555-555-5555","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("LaimGallagher","Laim","Gallagher","LaimGallagher@email.com","444-444-4444","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("PaulMcCartney","Paul","McCartney","PaulMcCartney@email.com","333-333-3333","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("KimCobain","Kim","Cobain","KimCobain@email.com","222-222-2222","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("JohnLennon","John","Lennon","JohnLennon@email.com","111-111-1111","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("PaulMcCartney","Paul","McCartney","PaulMcCartney@email.com","999-999-9999","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("DonHenley","Don","Henly","DonHenley@email.com","888-888-8888","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("PhilCollins","Phil","Collins","PhilCollins@email.com","777-777-7777","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("JimmyPaige","Jimmy","Paige","JimmyPaige@email.com","666-666-6666","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("TrevorHurst","Trevor","Hurst","TrevorHurst@email.com","123-456-7890","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("AdamLevine","Adam","Levine","AdamLevine@email.com","234-567-8901","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("AxlRose","Axl","Rose","AxlRose@email.com","345-678-9012","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("ChadKroeger","Chad","Kroeger","ChadKroeger@email.com","456-789-0123","Password");
        System.out.println(mc.add(aMember));
        aMember = new Member("DaveGrohl","Dave","Grohl","DaveGrohl@email.com","567-890-1234","Password");
        System.out.println(mc.add(aMember));
        List<String> memberList = new ArrayList<>(Arrays.asList(mc.getMembers()));
        final MemberCollection mcf = mc;


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
