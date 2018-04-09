package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyHistoryEventsFragment extends Fragment {

    private ViewGroup donationInfoLayout;
    private ListView lv;
    Context ctx;
    View historyEventsView;
    private static final String TAG = "MyHistoryActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle("My History/Events");
        return inflater.inflate(R.layout.fragment_my_history_events, container, false);
    }

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

        /**
         * Event Info load
         */
        Member aMember;
        aMember = new Member("93471", "Test", "Event", "test@gmail.com", "987-654-3210");
        final Member selectedMember = aMember;
        lv = (ListView) historyEventsView.findViewById(R.id.associated_events_table);

        //event array
        final EventCollection ecf = new EventCollection(ctx);
        ArrayList<String> eventList = ecf.getAssociatedEvents(selectedMember.getUserID());

        //make array adapter
        if (eventList != null && !eventList.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, eventList);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = lv.getItemAtPosition(position);
                    String myItem = item.toString();
                    Intent intent = new Intent(ctx, ViewEventActivity.class);
                    intent.putExtra("SelectedEvent", ecf.get(myItem));
                    startActivity(intent);
                }
            });
        } else {
            eventList.add("You have no associated events.");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    eventList);
            lv.setAdapter(arrayAdapter);
            lv.setEnabled(false);
        }
    }

    private void addDonationInfoLine(String leftText, String rightText) {
        View layout3 = LayoutInflater.from(getActivity()).inflate(R.layout.about_you_list_item, donationInfoLayout, false);

        TextView textViewLeft = (TextView) layout3.findViewById(R.id.text_left);
        TextView textView1Right = (TextView) layout3.findViewById(R.id.text_right);

        textViewLeft.setText(leftText);
        textView1Right.setText(rightText);

        donationInfoLayout.addView(layout3);
    }

    private void addBorder(ViewGroup viewToAdd) {
        View tableBorder = LayoutInflater.from(getActivity()).inflate(R.layout.table_separator, viewToAdd, false);
        viewToAdd.addView(tableBorder);
    }

}
