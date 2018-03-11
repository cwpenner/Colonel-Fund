package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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
    private EditText searchBar = null;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_member_list);
        searchBar = (EditText) findViewById(R.id.editText);
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
        lv.setTextFilterEnabled(true);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text [" + s + "]");
                arrayAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
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
 * Filterable Member list adapter class. Allows the array to be search by custom variables.
 */
class MemberListAdapter extends ArrayAdapter<MemberListModel> implements Filterable {
    private final Context context;
    private ArrayList<MemberListModel> originalArrayList;
    private ArrayList<MemberListModel> filteredModelsArrayList;
    private ItemFilter eFilter = new ItemFilter();

    /**
     * Constructor for member list item adapter.
     *
     * @param context
     * @param data
     */
    public MemberListAdapter(Context context, ArrayList<MemberListModel> data) {
        super(context, R.layout.member_list_item, data);
        this.context = context;
        this.originalArrayList = new ArrayList<MemberListModel>(data);
        this.filteredModelsArrayList = new ArrayList<MemberListModel>(data);
    }

    /**
     * Gets View for Member List Item.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return MemberListView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.member_list_item, parent, false);

            holder.memberView = inflater.inflate(R.layout.member_list_item, parent, false);
            convertView.setTag(holder);
        } else {
            holder.memberView = convertView;
        }
        // view holders for information
        TextView memberInitials = (TextView) holder.memberView.findViewById(R.id.member_initials);
        TextView memberName = (TextView) holder.memberView.findViewById(R.id.memberName);
        TextView memberID = (TextView) holder.memberView.findViewById(R.id.userID);

        memberInitials.setText(filteredModelsArrayList.get(position).getInitials());
        memberName.setText(filteredModelsArrayList.get(position).getFullName());
        memberID.setText(filteredModelsArrayList.get(position).getUserID());
        return holder.memberView;
    }

    /**
     * Holds MemberView for filterable Members
     */
    static class ViewHolder {
        View memberView;
    }

    /**
     * Gets filter for Members
     *
     * @return MemberFilter
     */
    public Filter getFilter() {
        return eFilter;
    }

    /**
     * Overrides ItemFilter class in order to filter by custom variables in an Member object.
     */
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final ArrayList<MemberListModel> list = new ArrayList<MemberListModel>(originalArrayList);
            System.out.println("Original Array List Size: " + originalArrayList.size());
            int count = list.size();
            final ArrayList<MemberListModel> nlist = new ArrayList<MemberListModel>(count);
            MemberListModel filterableModel;
            for (int i = 0; i < count; i++) {
                filterableModel = list.get(i);
                if (filterableModel.getFirstName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableModel);
                    System.out.println("Added Member: " + filterableModel.getFullName());
                } else if (filterableModel.getLastName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableModel);
                    System.out.println("Added Member: " + filterableModel.getFullName());
                } else if (filterableModel.getFullName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableModel);
                    System.out.println("Added Member: " + filterableModel.getFullName());
                } else if (filterableModel.getUserID().toLowerCase().contains(filterString)) {
                    nlist.add(filterableModel);
                    System.out.println("Added Member: " + filterableModel.getFullName());
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        /**
         * Override filters publish results for array list
         *
         * @param constraint
         * @param results
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredModelsArrayList = (ArrayList<MemberListModel>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredModelsArrayList.size(); i < l; i++)
                add((MemberListModel) filteredModelsArrayList.get(i));
            notifyDataSetInvalidated();
        }

        /**
         * Get the size of a filtered array list
         *
         * @return arrayListSize
         */
        public int getCount() {
            if (filteredModelsArrayList == null) {
                return 0;
            } else {
                return filteredModelsArrayList.size();
            }
        }

        /**
         * Get a filterable items position
         *
         * @param position
         * @return itemPosition
         */
        public MemberListModel getItem(int position) {
            return filteredModelsArrayList.get(position);
        }

        /**
         * Get a filterable items ID
         *
         * @param position
         * @return itemID
         */
        public long getItemId(int position) {
            return position;
        }
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

