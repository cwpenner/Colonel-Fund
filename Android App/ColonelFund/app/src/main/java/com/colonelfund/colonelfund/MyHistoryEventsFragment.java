package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Fragment that allows a user to view their history. Instantiated in Main Activity.
 */
public class MyHistoryEventsFragment extends Fragment {
    private ViewGroup donationInfoLayout;
    private ListView lv;
    Context ctx;
    View historyEventsView;
    private static final String TAG = "MyHistoryActivity";

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
        getActivity().setTitle("My History/Events");
        return inflater.inflate(R.layout.fragment_my_history_events, container, false);
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
        historyEventsView = getView();

        donationInfoLayout = (ViewGroup) historyEventsView.findViewById(R.id.history_table);
        addDonationInfoLine("Donation History Here", "  ");
        addBorder(donationInfoLayout);
        addDonationInfoLine("Example Event Title", "$15.02");
        addBorder(donationInfoLayout);
        addDonationInfoLine("Example Event Title 2", "$1.02");

        //loads info for member
        Member aMember;
        aMember = new Member("93471", "Test", "Event", "test@gmail.com", "987-654-3210", "tester", "", "", "", "");
        final Member selectedMember = aMember;
        lv = (ListView) historyEventsView.findViewById(R.id.associated_events_table);

        //event array
        final EventCollection ecf = new EventCollection(ctx);
        ArrayList<String> eventListString = ecf.getAssociatedEvents(User.getCurrentUser().getUserID());
        ArrayList<Event> eventList = new ArrayList<>();

        for (String eventString : eventListString) {
            Event event = ecf.get(eventString);
            eventList.add(event);
        }

        //make array adapter
        if (eventList != null && !eventList.isEmpty()) {
            //make array adapter
            ArrayAdapter arrayAdapter = new EventListAdapter(ctx, generateData(eventList));
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
        } else {
            eventListString.add("This user has no associated events.");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    ctx,
                    android.R.layout.simple_list_item_1,
                    eventListString);
            lv.setAdapter(arrayAdapter);
            lv.setEnabled(false);
        }
    }

    /**
     * Adds text to donation line of table.
     *
     * @param leftText text for left side of table.
     * @param rightText text for right side of table.
     */
    private void addDonationInfoLine(String leftText, String rightText) {
        View layout3 = LayoutInflater.from(getActivity()).inflate(R.layout.about_you_list_item, donationInfoLayout, false);

        TextView textViewLeft = (TextView) layout3.findViewById(R.id.text_left);
        TextView textView1Right = (TextView) layout3.findViewById(R.id.text_right);

        textViewLeft.setText(leftText);
        textView1Right.setText(rightText);

        donationInfoLayout.addView(layout3);
    }

    /**
     * Adds border to view group.
     *
     * @param viewToAdd view group to add.
     */
    private void addBorder(ViewGroup viewToAdd) {
        View tableBorder = LayoutInflater.from(getActivity()).inflate(R.layout.table_separator, viewToAdd, false);
        viewToAdd.addView(tableBorder);
    }

    /**
     * Generates List Details for Event List.
     *
     * @param eventList
     * @return eventModel
     */
    private ArrayList<EventListModel> generateData(Collection eventList) {
        ArrayList<EventListModel> models = new ArrayList<EventListModel>();
        Iterator<Event> EventItr = eventList.iterator();
        while (EventItr.hasNext()) {
            Event temp = EventItr.next();
            double goalProgress;
            if ((temp.getCurrentFunds() / temp.getFundGoal()) < 1) {
                goalProgress = (temp.getCurrentFunds() / temp.getFundGoal());
            } else {
                goalProgress = 1;
            }
            models.add(new EventListModel(temp.getTitle(), temp.getType(), temp.getAssociatedMember(),
                    temp.getAssociatedEmail(), temp.getEventDate(), goalProgress, temp.getDescription()));
        }
        return models;
    }
}
