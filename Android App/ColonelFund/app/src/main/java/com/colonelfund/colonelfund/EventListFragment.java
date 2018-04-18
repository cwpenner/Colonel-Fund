package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
 * Fragment that allows a user to view current events. Instantiated in Main Activity.
 */
public class EventListFragment extends Fragment {
    private ListView lv = null;
    private ArrayAdapter arrayAdapter = null;
    private EditText searchBar = null;
    Context ctx;
    View eventListView;
    private static final String TAG = "EventListFragment";

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
        getActivity().setTitle("Event List");
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    /**
     * Overrides on create in order to draw event list and sets listeners for buttons and search.
     * @param savedInstanceState saved state of fragment.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ctx = getActivity();
        eventListView = getView();
        searchBar = eventListView.findViewById(R.id.editText);
        final SwipeRefreshLayout swiperefresh = eventListView.findViewById(R.id.swiperefresh);
        lv = eventListView.findViewById(R.id.eventListView);
        final EventCollection ecf = new EventCollection(ctx);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventCollection ec = new EventCollection(ctx);
                ec.updateFromRemote();
                swiperefresh.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperefresh.setRefreshing(false);
                        EventCollection newEcf = new EventCollection(ctx);
                        arrayAdapter = new EventListAdapter(ctx, newEcf.generateListData());
                        lv.setAdapter(arrayAdapter);
                        arrayAdapter.getFilter().filter(searchBar.getText());
                    }
                }, 3000);
            }
        });

        //make array adapter
        arrayAdapter = new EventListAdapter(ctx, ecf.generateListData());
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
        lv.setTextFilterEnabled(true);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text [" + s + "]");
                arrayAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
