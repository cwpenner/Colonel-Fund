package com.colonelfund.colonelfund;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Filterable Event list adapter class. Allows the array to be search by custom variables.
 */
class EventListAdapter extends ArrayAdapter<EventListModel> implements Filterable {
    private final Context context;
    private ArrayList<EventListModel> originalArrayList;
    private ArrayList<EventListModel> filteredModelsArrayList;
    private ItemFilter eFilter = new ItemFilter();
    private final String[] months = {"J\nA\nN",
            "F\nE\nB",
            "M\nA\nR",
            "A\nP\nR",
            "M\nA\nY",
            "J\nU\nN",
            "J\nU\nL",
            "A\nU\nG",
            "S\nE\nP",
            "O\nC\nT",
            "N\nO\nV",
            "D\nE\nC"
    };

    /**
     * Constructor for member list item adapter.
     *
     * @param context for fragment
     * @param data for event list
     */
    public EventListAdapter(Context context, ArrayList<EventListModel> data) {
        super(context, R.layout.event_list_item, data);
        this.context = context;
        this.originalArrayList = new ArrayList<EventListModel>(data);
        this.filteredModelsArrayList = new ArrayList<EventListModel>(data);
    }

    /**
     * Gets View for Event List Item.
     *
     * @param position of line item
     * @param convertView for event list item
     * @param parent of event list
     * @return EventListView
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

        MemberCollection mc = new MemberCollection(context);
        Member member = mc.get(filteredModelsArrayList.get(position).getAssociatedMember());

        // view holders for information
        TextView eventName = (TextView) holder.eventView.findViewById(R.id.memberName);
        TextView eventMember = (TextView) holder.eventView.findViewById(R.id.userID);
        ProgressBar goalProgress = (ProgressBar) holder.eventView.findViewById(R.id.goalProgress);
        ImageView eventType = (ImageView) holder.eventView.findViewById(R.id.member_initials);
        TextView eventDay = (TextView) holder.eventView.findViewById(R.id.event_day_box);
        TextView eventMonth = (TextView) holder.eventView.findViewById(R.id.event_month_box);
        //set main view to specific view holders
        eventName.setText(filteredModelsArrayList.get(position).getTitle());
        eventMember.setText(member.getFormattedFullName());
        goalProgress.setProgress(filteredModelsArrayList.get(position).getGoalProgress().intValue());
        String eventDate = filteredModelsArrayList.get(position).getEventDate();
        eventDay.setText(eventDate.substring((eventDate.length() - 2), (eventDate.length())));
        String monthString = eventDate.substring((eventDate.length() - 5), (eventDate.length() - 3));
        int month = Integer.parseInt(monthString);
        eventMonth.setText(months[month - 1]);
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

    /**
     * Holds eventView for filterable events
     */
    static class ViewHolder {
        View eventView;
    }

    /**
     * Gets filter for Events
     *
     * @return eventFilter
     */
    public Filter getFilter() {
        return eFilter;
    }

    /**
     * Overrides ItemFilter class in order to filter by custom variables in an event object.
     */
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
                } else if (filterableModel.getType().toLowerCase().contains(filterString)) {
                    nlist.add(filterableModel);
                    System.out.println("Added event: " + filterableModel.getTitle());
                } else if (filterableModel.getAssociatedEmail().toLowerCase().contains(filterString)) {
                    nlist.add(filterableModel);
                    System.out.println("Added event: " + filterableModel.getTitle());
                } else if (filterableModel.getEventDate().toLowerCase().contains(filterString)) {
                    nlist.add(filterableModel);
                    System.out.println("Added event: " + filterableModel.getTitle());
                } else if (filterableModel.getEventDesc().toLowerCase().contains(filterString)) {
                    nlist.add(filterableModel);
                    System.out.println("Added event: " + filterableModel.getTitle());
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        /**
         * Override filters publish results for array list
         *
         * @param constraint for filter
         * @param results of filter
         */
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
        public EventListModel getItem(int position) {
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
