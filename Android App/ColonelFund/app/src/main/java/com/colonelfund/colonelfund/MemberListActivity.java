package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private ArrayAdapter arrayAdapter = null;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_member_list);

        lv = (ListView) findViewById(R.id.memberListView);

        final SwipeRefreshLayout swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        final MemberCollection mcf = new MemberCollection(getApplicationContext());
        Collection<Member> memberList = mcf.getMembersList();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MemberCollection mc = new MemberCollection(getApplicationContext());
                mc.updateFromRemote();
                swiperefresh.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperefresh.setRefreshing(false);
                        MemberCollection newMcf = new MemberCollection(getApplicationContext());
                        Collection<Member> newMemberList = newMcf.getMembersList();
                        arrayAdapter = new MemberListAdapter(ctx, generateData(newMemberList));
                        lv.setAdapter(arrayAdapter);
                    }
                },3000);
            }
        });

        //make array adapter
        arrayAdapter = new MemberListAdapter(this, generateData(memberList));
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
     * @param menu
     * @return
     */
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
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

/**
 * Member list Item Model class.
 */
class MemberListModel {
    private String userID;
    private String initials;
    private String firstName;
    private String lastName;

    /**
     * Constructor for Initials circle.
     * @param initials
     * @param userID
     */
    public MemberListModel(String initials, String userID, String firstName, String lastName) {
        super();
        this.initials = initials;
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getInitials() {
        return initials;
    }
    public String getUserID() {
        return userID;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }

}

/**
 * Member list adapter class.
 */
class MemberListAdapter extends ArrayAdapter<MemberListModel> {

    private final Context context;
    private final ArrayList<MemberListModel> modelsArrayList;

    /**
     * Constructor for member list item adapter.
     * @param context
     * @param modelsArrayList
     */
    public MemberListAdapter(Context context, ArrayList<MemberListModel> modelsArrayList) {
        super(context, R.layout.member_list_item, modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    /**
     * Gets View for Member List Item.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = null;
        rowView = inflater.inflate(R.layout.member_list_item, parent, false);

        TextView memberInitials = (TextView) rowView.findViewById(R.id.member_initials);
        TextView memberName = (TextView) rowView.findViewById(R.id.memberName);
        TextView memberID = (TextView) rowView.findViewById(R.id.userID);

        memberInitials.setText(modelsArrayList.get(position).getInitials());
        memberName.setText(modelsArrayList.get(position).getFullName());
        memberID.setText(modelsArrayList.get(position).getUserID());

        return rowView;
    }
}
