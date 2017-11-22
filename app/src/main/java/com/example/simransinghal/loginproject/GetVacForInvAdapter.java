package com.example.simransinghal.loginproject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simran Singhal on 22-11-2017.
 */

public class GetVacForInvAdapter extends BaseAdapter{
    LayoutInflater mInflater;

    public static final int MaxItems = 50;
    List<String> vname = new ArrayList<String>();
    List<String> vid = new ArrayList<String>();
    List<String> stock = new ArrayList<String>();



    public GetVacForInvAdapter(LayoutInflater inflater, List<String> array, List<String> vid, List<String> stock) {
        mInflater = inflater;
        this.vname = array;
        this.stock = stock;
        this.vid = vid;
//        this.context = c.getActivity();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.inv_row, parent, false);

        }


        TextView name = (TextView)convertView.findViewById(R.id.vacc_name);
        TextView avail = (TextView)convertView.findViewById(R.id.available);

//        ImageView edit = (ImageView) convertView.findViewById(R.id.iv_edit);
//
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               Log.d("clicked on",Integer.toString(position));
//
//                //Inventory inv_obj = new Inventory();
//                //inv_obj.click();
//
//
//
//            }
//        });


        name.setText(vname.get(position));
        avail.setText(stock.get(position));
        Log.d("name child list",vname.get(position)+stock.get(position));
        return convertView;
    }





}
