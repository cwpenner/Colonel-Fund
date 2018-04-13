package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Fragment that allows a user to view current members. Instantiated in Main Activity.
 */
public class MemberListFragment extends Fragment {
    private ListView lv;
    private ArrayAdapter arrayAdapter = null;
    private EditText searchBar = null;
    Context ctx;
    View memberListView;
    private static final String TAG = "MemberListFragment";

    /**
     * Overrides on create to initialize variables for page.
     *
     * @param inflater inflater for fragment views
     * @param container view group for fragment.
     * @param savedInstanceState saved state of fragment.
     * @return view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle("Member List");
        return inflater.inflate(R.layout.fragment_member_list, container, false);
    }

    /**
     * overrides on-create for fragment to initialize values for view.
     *
     * @param savedInstanceState saved state of fragment.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ctx = getActivity();
        memberListView = getView();
        searchBar = (EditText) memberListView.findViewById(R.id.editText);
        lv = (ListView) memberListView.findViewById(R.id.memberListView);
        final SwipeRefreshLayout swiperefresh = (SwipeRefreshLayout) memberListView.findViewById(R.id.swiperefresh);
        final MemberCollection mcf = new MemberCollection(ctx);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MemberCollection mc = new MemberCollection(ctx);
                mc.updateFromRemote();
                swiperefresh.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperefresh.setRefreshing(false);
                        MemberCollection newMcf = new MemberCollection(ctx);
                        arrayAdapter = new MemberListAdapter(ctx, newMcf.generateListData());
                        lv.setAdapter(arrayAdapter);
                        arrayAdapter.getFilter().filter(searchBar.getText());
                    }
                },3000);
            }
        });
        //make array adapter
        arrayAdapter = new MemberListAdapter(ctx, mcf.generateListData());
        lv.setAdapter(arrayAdapter);
        // Add listeners for each list item.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemberListModel item = (MemberListModel) lv.getItemAtPosition(position);
                String myItem = item.getUserID();
                Intent intent = new Intent(ctx, ViewMemberActivity.class);
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
}