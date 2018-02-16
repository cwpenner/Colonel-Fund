package com.colonelfund.colonelfund;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Event list view class.
 */
public class EventListActivity extends AppCompatActivity {
    private ListView lv = null;
    private ArrayAdapter arrayAdapter =  null;
    private EditText searchBar = null;

    /**
     * Draws event list
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_event_list);
        searchBar = (EditText) findViewById(R.id.editText);

        lv = (ListView) findViewById(R.id.eventListView);

        //dummy array
        final EventCollection ecf = new EventCollection(getApplicationContext());
        Collection<Event> eventList = ecf.getEventsList();

        //make array adapter
        arrayAdapter = new EventListAdapter(this, generateData(eventList));
        lv.setAdapter(arrayAdapter);

        // set listener for each item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                EventListModel item = (EventListModel) lv.getItemAtPosition(position);
                String myItem = item.getTitle();
                Intent intent = new Intent(EventListActivity.this, ViewEventActivity.class);
                intent.putExtra("SelectedEvent", ecf.get(myItem));
                startActivity(intent);
            }
        });
        lv.setTextFilterEnabled(true);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text ["+s+"]");

                arrayAdapter.getFilter().filter(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

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
     * Generates Initials and User Name for Event List.
     *
     * @param eventList
     * @return
     */
    private ArrayList<EventListModel> generateData(Collection eventList) {
        ArrayList<EventListModel> models = new ArrayList<EventListModel>();
        Iterator<Event> EventItr = eventList.iterator();
        while (EventItr.hasNext()) {
            Event temp = EventItr.next();
            double goalProgress;
            if ((temp.getCurrentFunds()/temp.getFundGoal()) < 1) {
                goalProgress = (temp.getCurrentFunds()/temp.getFundGoal());
            } else {
                goalProgress = 1;
            }
            models.add(new EventListModel(temp.getTitle(),temp.getType(),temp.getAssociatedMember(),
                    temp.getEventDate(),goalProgress));
        }
        return models;
    }

}

/**
 * Event list adapter class.
 */
class EventListAdapter extends ArrayAdapter<EventListModel> implements Filterable {

    private final Context context;
    private ArrayList<EventListModel> originalArrayList;
    private ArrayList<EventListModel> filteredModelsArrayList;
    private ItemFilter mFilter = new ItemFilter();

    /**
     * Constructor for member list item adapter.
     * @param context
     * @param data
     */
    public EventListAdapter(Context context, ArrayList<EventListModel> data) {
        super(context, R.layout.event_list_item, data);
        this.context = context;
        this.originalArrayList = new ArrayList<EventListModel>(data);
        this.filteredModelsArrayList = new ArrayList<EventListModel>(data);
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

        ViewHolder holder;
        holder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_list_item, parent, false);

            holder.eventView = inflater.inflate(R.layout.event_list_item, parent, false);
            convertView.setTag(holder);
        } else {
            holder.eventView = convertView;
        }

        /**
         *

        View rowView = null;
        rowView = inflater.inflate(R.layout.event_list_item, parent, false);

        TextView eventName = (TextView) rowView.findViewById(R.id.eventName);
        TextView eventMember = (TextView) rowView.findViewById(R.id.eventUser);
        ProgressBar goalProgress = (ProgressBar) rowView.findViewById(R.id.goalProgress);
        ImageView eventType = (ImageView) rowView.findViewById(R.id.event_type_pic);

        eventName.setText(origionalArrayList.get(position).getTitle());
        eventMember.setText(origionalArrayList.get(position).getAssociatedMember());
        goalProgress.setProgress(origionalArrayList.get(position).getGoalProgress().intValue());
        if (origionalArrayList.get(position).getType().equalsIgnoreCase("bbq")) {
            eventType.setImageResource(R.drawable.bbq);
        } else if (origionalArrayList.get(position).getType().equalsIgnoreCase("emergency")) {
            eventType.setImageResource(R.drawable.emergency);
        } else if (origionalArrayList.get(position).getType().equalsIgnoreCase("medical")) {
            eventType.setImageResource(R.drawable.medical);
        } else if (origionalArrayList.get(position).getType().equalsIgnoreCase("party")) {
            eventType.setImageResource(R.drawable.party);
        } else if (origionalArrayList.get(position).getType().equalsIgnoreCase("unknown")) {
            eventType.setImageResource(R.drawable.unknown);
        } else {
            eventType.setImageResource(R.drawable.question);
        }

        return rowView;

         */
        //holder.eventView = inflater.inflate(R.layout.event_list_item, parent, false);

        TextView eventName = (TextView) holder.eventView.findViewById(R.id.eventName);
        TextView eventMember = (TextView) holder.eventView.findViewById(R.id.eventUser);
        ProgressBar goalProgress = (ProgressBar) holder.eventView.findViewById(R.id.goalProgress);
        ImageView eventType = (ImageView) holder.eventView.findViewById(R.id.event_type_pic);

        eventName.setText(filteredModelsArrayList.get(position).getTitle());
        eventMember.setText(filteredModelsArrayList.get(position).getAssociatedMember());
        goalProgress.setProgress(filteredModelsArrayList.get(position).getGoalProgress().intValue());
        if (filteredModelsArrayList.get(position).getType().equalsIgnoreCase("bbq")) {
            eventType.setImageResource(R.drawable.bbq);
        } else if (filteredModelsArrayList.get(position).getType().equalsIgnoreCase("emergency")) {
            eventType.setImageResource(R.drawable.emergency);
        } else if (filteredModelsArrayList.get(position).getType().equalsIgnoreCase("medical")) {
            eventType.setImageResource(R.drawable.medical);
        } else if (filteredModelsArrayList.get(position).getType().equalsIgnoreCase("party")) {
            eventType.setImageResource(R.drawable.party);
        } else if (filteredModelsArrayList.get(position).getType().equalsIgnoreCase("unknown")) {
            eventType.setImageResource(R.drawable.unknown);
        } else {
            eventType.setImageResource(R.drawable.question);
        }
        return holder.eventView;
    }


    static class ViewHolder {
        View eventView;
    }

    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<EventListModel> list = new ArrayList<EventListModel>(originalArrayList);

            System.out.println("Original Array List Size: " + originalArrayList.size());

            int count = list.size();
            final ArrayList<EventListModel> nlist = new ArrayList<EventListModel>(count);

            EventListModel filterableModel;

            for (int i = 0; i < count; i++) {
                filterableModel = list.get(i);
                if (filterableModel.getTitle().toLowerCase().contains(filterString)) {
                    nlist.add(filterableModel);
                    System.out.println("Added event: " + filterableModel.getTitle());
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredModelsArrayList = (ArrayList<EventListModel>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredModelsArrayList.size(); i < l; i++)
                add((EventListModel) filteredModelsArrayList.get(i));
            notifyDataSetInvalidated();
        }

        public int getCount() {
            if(filteredModelsArrayList==null){
                return 0;
            }else{
                return filteredModelsArrayList.size();
            }
        }

        public EventListModel getItem(int position) {
            return filteredModelsArrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }
    }
}

/**
 * Event list Item Model class.
 */
class EventListModel {
    private String title;
    private String type;
    private String associatedMember;
    private String eventDate;
    private Double goalProgress;

    /**
     * Constructor for Initials circle.
     * @param title
     * @param type
     */
    public EventListModel(String title, String type, String associatedMember, String eventDate, Double goalProgress) {
        super();
        this.title = title;
        this.type = type;
        this.associatedMember = associatedMember;
        this.eventDate = eventDate;
        this.goalProgress = (goalProgress*100);
    }

    public String getType() {
        return type;
    }
    public String getTitle() {
        return title;
    }
    public String getAssociatedMember() {
        return associatedMember;
    }
    public String getEventDate() {
        return eventDate;
    }
    public Double getGoalProgress() {
        return goalProgress;
    }

}

