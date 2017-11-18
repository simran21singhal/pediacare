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
 * Created by Simran Singhal on 17-11-2017.
 */

public class VaccListAdapter extends BaseAdapter {
    LayoutInflater mInflater;

    public static final int MaxItems = 50;
    List<String> vname = new ArrayList<String>();
    List<String> given = new ArrayList<String>();
    List<String> due = new ArrayList<String>();


    public VaccListAdapter(LayoutInflater inflater, List<String> name, List<String> given_on,List<String> due_on) {
        mInflater = inflater;
        this.given = given_on;
        this.due = due_on;
        this.vname = name;
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
            convertView = mInflater.inflate(R.layout.vacclistrow, parent, false);

        }

        TextView vacname = (TextView)convertView.findViewById(R.id.tv_vacc);
        TextView givendate = (TextView)convertView.findViewById(R.id.tv_givenon);
        TextView duedate = (TextView)convertView.findViewById(R.id.tv_dueon);



        vacname.setText(vname.get(position));
        givendate.setText(given.get(position));
        duedate.setText(due.get(position));
        Log.d("vacc list",vname.get(position)+given.get(position)+due.get(position));
        return convertView;
    }

}
