package com.colonelfund.colonelfund;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyMemberAdapter extends ArrayAdapter<MemberModel> {

    private final Context context;
    private final ArrayList<MemberModel> modelsArrayList;

    public MyMemberAdapter(Context context, ArrayList<MemberModel> modelsArrayList) {

        super(context, R.layout.member_list_item, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = null;
        rowView = inflater.inflate(R.layout.member_list_item, parent, false);

        TextView memberInitials = (TextView) rowView.findViewById(R.id.member_initials);
        TextView memberID = (TextView) rowView.findViewById(R.id.member_id);

        memberInitials.setText(modelsArrayList.get(position).getInitials());
        memberID.setText(modelsArrayList.get(position).getMemberID());

        return rowView;
    }
}