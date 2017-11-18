package com.example.simransinghal.loginproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.SharedPrefrences;

/**
 * Created by Simran Singhal on 17-11-2017.
 */

public class GetChildListAdapter extends BaseAdapter {


    LayoutInflater mInflater;

    public static final int MaxItems = 50;
List<String> cname = new ArrayList<String>();
    List<String> cid = new ArrayList<String>();


    public GetChildListAdapter(LayoutInflater inflater, List<String> array,List<String> cid) {
        mInflater = inflater;
        this.cname = array;
        this.cid = cid;
    }

    @Override


    public int getCount() {
        return cname.size();
    }

    @Override
    public Object getItem(int position) {
        if ((position + 1) % 2 == 0) {
            return "I am whatsapp at " + (position + 1);
        }
        return "I am Launcher at " + (position + 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row, parent, false);

        }

        TextView name ;
        name = (TextView)convertView.findViewById(R.id.tv_name);


        name.setText(cname.get(position));
        Log.d("name child list",cname.get(position));
        return convertView;
    }





}
