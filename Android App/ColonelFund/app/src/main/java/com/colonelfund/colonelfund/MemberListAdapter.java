package com.colonelfund.colonelfund;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Member list adapter class.
 */
public class MemberListAdapter extends ArrayAdapter<MemberListModel> {

    private final Context context;
    private final ArrayList<MemberListModel> modelsArrayList;

    /**
     * Constructor for member list item adapter.
     * @param context
     * @param modelsArrayList
     */
    public MemberListAdapter(Context context, ArrayList<MemberListModel> modelsArrayList) {
        super(context, R.layout.member_list_item, modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
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

        View rowView = null;
        rowView = inflater.inflate(R.layout.member_list_item, parent, false);

        TextView memberInitials = (TextView) rowView.findViewById(R.id.member_initials);
        TextView memberName = (TextView) rowView.findViewById(R.id.memberName);

        memberInitials.setText(modelsArrayList.get(position).getInitials());
        memberName.setText(modelsArrayList.get(position).getFullName());

        return rowView;
    }
}