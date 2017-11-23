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
 * Created by Simran Singhal on 22-11-2017.
 */

public class GetVaccAccDate extends BaseAdapter {
    LayoutInflater mInflater;

    public static final int MaxItems = 50;
    List<String> name = new ArrayList<String>();
    List<String> count = new ArrayList<String>();
    List<Integer> list_sno = new ArrayList<Integer>();



    public GetVaccAccDate(LayoutInflater inflater, List<String> name,List<String> count,List<Integer> no) {
        mInflater = inflater;
        this.name = name;
        this.count = count;
        this.list_sno=no;
    }

    @Override


    public int getCount() {
        return name.size();
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
        Log.d("value","vname: "+name.get(position)+" count: "+count.get(position));

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.consumption_row, parent, false);

        }

        TextView vname = (TextView)convertView.findViewById(R.id.vacc_name);
        TextView tv_count = (TextView)convertView.findViewById(R.id.tv_count);
        TextView sno = (TextView)convertView.findViewById(R.id.tv_sno);


        Log.d("hello", "hello");
        String num = Integer.toString(list_sno.get(position));

        vname.setText(name.get(position));
        tv_count.setText(count.get(position));
        sno.setText(num);

        Log.d("name child list",name.get(position));
        return convertView;
    }

}
