package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Fragment that allows a user to view current main information. Instantiated in Main Activity.
 */
public class MainPageFragment extends Fragment {
    private static final String TAG = "MainPageFragment";
    private ListView eventListView = null;
    private ListView memberListView = null;
    private ArrayAdapter eventArrayAdapter = null;
    private ArrayAdapter memberArrayAdapter = null;
    Context ctx;
    View mainView;

    /**
     * Overrides on create to initialize variables for page.
     * @param inflater inflater for fragment views
     * @param container view group for fragment.
     * @param savedInstanceState saved state of fragment.
     * @return view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle("Main");
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    /**
     * Overrides on create in order to draw event list and sets listeners for buttons and search.
     * @param savedInstanceState saved state of fragment.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ctx = getActivity();
        mainView = getView();
        final SwipeRefreshLayout eventSwipeRefresh = mainView.findViewById(R.id.featuredEventsBox);
        eventListView = mainView.findViewById(R.id.eventListView);
        final EventCollection ecf = new EventCollection(ctx);
        eventSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventCollection ec = new EventCollection(ctx);
                ec.updateFromRemote();
                eventSwipeRefresh.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        eventSwipeRefresh.setRefreshing(false);
                        EventCollection newEcf = new EventCollection(ctx);
                        eventArrayAdapter = new EventListAdapter(ctx, newEcf.generateTop3ListData());
                        eventListView.setAdapter(eventArrayAdapter);
                    }
                }, 3000);
            }
        });
        //make array adapter
        eventArrayAdapter = new EventListAdapter(ctx, ecf.generateTop3ListData());
        eventListView.setAdapter(eventArrayAdapter);
        // set listener for each item
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventListModel item = (EventListModel) eventListView.getItemAtPosition(position);
                String myItem = item.getTitle();
                Intent intent = new Intent(ctx, ViewEventActivity.class);
                intent.putExtra("SelectedEvent", ecf.get(myItem));
                startActivity(intent);
            }
        });

        final SwipeRefreshLayout memberSwipeRefresh = mainView.findViewById(R.id.topContributorsBox);
        memberListView = mainView.findViewById(R.id.memberListView);
        final MemberCollection mcf = new MemberCollection(ctx);
        memberSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MemberCollection mc = new MemberCollection(ctx);
                mc.updateFromRemote();
                memberSwipeRefresh.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        memberSwipeRefresh.setRefreshing(false);
                        MemberCollection newMcf = new MemberCollection(ctx);
                        eventArrayAdapter = new MemberListAdapter(ctx, newMcf.generateTop3ListData());
                        memberListView.setAdapter(memberArrayAdapter);
                    }
                }, 3000);
            }
        });
        //make array adapter
        memberArrayAdapter = new MemberListAdapter(ctx, mcf.generateTop3ListData());
        memberListView.setAdapter(memberArrayAdapter);
        // set listener for each item
        memberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemberListModel item = (MemberListModel) memberListView.getItemAtPosition(position);
                String myItem = item.getUserID();
                Intent intent = new Intent(ctx, ViewMemberActivity.class);
                intent.putExtra("SelectedMember", mcf.get(myItem));
                startActivity(intent);
            }
        });
    }

    /**
     * Overrides on create for fragment.
     * @param savedInstanceState Saved state of fragment instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
