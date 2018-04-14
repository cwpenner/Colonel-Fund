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
    private ListView lv = null;
    private ArrayAdapter arrayAdapter = null;
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
        final SwipeRefreshLayout swipeRefresh = mainView.findViewById(R.id.featuredEventsBox);
        lv = mainView.findViewById(R.id.eventListView);
        final EventCollection ecf = new EventCollection(ctx);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventCollection ec = new EventCollection(ctx);
                ec.updateFromRemote();
                swipeRefresh.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        EventCollection newEcf = new EventCollection(ctx);
                        arrayAdapter = new EventListAdapter(ctx, newEcf.generateTop3ListData());
                        lv.setAdapter(arrayAdapter);
                    }
                }, 3000);
            }
        });

        //make array adapter
        arrayAdapter = new EventListAdapter(ctx, ecf.generateTop3ListData());
        lv.setAdapter(arrayAdapter);
        // set listener for each item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventListModel item = (EventListModel) lv.getItemAtPosition(position);
                String myItem = item.getTitle();
                Intent intent = new Intent(ctx, ViewEventActivity.class);
                intent.putExtra("SelectedEvent", ecf.get(myItem));
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
