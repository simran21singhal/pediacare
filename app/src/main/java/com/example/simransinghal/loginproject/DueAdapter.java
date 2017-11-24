package com.example.simransinghal.loginproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simran Singhal on 24-11-2017.
 */

public class DueAdapter extends BaseAdapter {
    LayoutInflater mInflater;

    public static final int MaxItems = 50;
    List<String> cname = new ArrayList<String>();
    List<String> vname = new ArrayList<String>();
    List<String> due = new ArrayList<String>();


    public DueAdapter(LayoutInflater inflater, List<String> vname, List<String> cname,List<String> due_on) {
        mInflater = inflater;
        this.cname = cname;
        this.due = due_on;
        this.vname = vname;
    }

    @Override


    public int getCount() {
        return vname.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.due_row, parent, false);

        }

        TextView childname = (TextView)convertView.findViewById(R.id.child_name);
        TextView vaccname = (TextView)convertView.findViewById(R.id.vacc_name);
        TextView duedate = (TextView)convertView.findViewById(R.id.tv_dueon);



        vaccname.setText(vname.get(position));
        childname.setText(cname.get(position));
        duedate.setText(due.get(position));
        Log.d("vacc list",vname.get(position)+cname.get(position)+due.get(position));
        return convertView;
    }
}
