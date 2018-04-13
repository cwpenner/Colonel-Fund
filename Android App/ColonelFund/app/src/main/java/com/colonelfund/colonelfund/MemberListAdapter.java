package com.colonelfund.colonelfund;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Filterable Member list adapter class. Allows the array to be search by custom variables.
 */
public class MemberListAdapter extends ArrayAdapter<MemberListModel> implements Filterable {
    private final Context context;
    private ArrayList<MemberListModel> originalArrayList;
    private ArrayList<MemberListModel> filteredModelsArrayList;
    private ItemFilter eFilter = new ItemFilter();

    /**
     * Constructor for member list item adapter.
     *
     * @param context context of fragment
     * @param data array list of members
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
     * @param position position of view
     * @param convertView view to convert
     * @param parent parent view group
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
        TextView memberEmail = (TextView) holder.memberView.findViewById(R.id.memberEmail);

        memberInitials.setText(filteredModelsArrayList.get(position).getInitials());
        memberName.setText(filteredModelsArrayList.get(position).getFullName());
        memberEmail.setText(filteredModelsArrayList.get(position).getEmail());
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
                } else if (filterableModel.getEmail().toLowerCase().contains(filterString)) {
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
         * @param constraint character sequence of filter.
         * @param results filter result of search.
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
         * @param position position of item
         * @return itemPosition
         */
        public MemberListModel getItem(int position) {
            return filteredModelsArrayList.get(position);
        }

        /**
         * Get a filterable items ID
         *
         * @param position position of filtered item
         * @return itemID
         */
        public long getItemId(int position) {
            return position;
        }
    }
}
