package com.example.simransinghal.loginproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

public class Parent extends Fragment {

    ListView list;
    ImageView fab;
    List<String> cname = new ArrayList<String>();
    List<String> childid = new ArrayList<String>();
    int pos;

    Fragment fragment;
    FragmentManager fragmentManager;


    String msg, status = "false";
    private SharedPrefrences fetch;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.parent, container, false);

        cname.clear();
        childid.clear();


        fetch = new SharedPrefrences(getContext());

        list = (ListView) v.findViewById(R.id.lv_list);

        fab = (ImageView) v.findViewById(R.id.fab);

        list.setVisibility(View.INVISIBLE);

        fragmentManager = getFragmentManager();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                fragment = fragmentManager.findFragmentByTag("parent");
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (fragment != null) {
                    fragmentTransaction.remove(fragment);
                }
                fragment = new ChildProfile();
                fragmentTransaction.add(R.id.fragment_replace, fragment, "profile");
                fragmentTransaction.addToBackStack("parent");
                fragmentTransaction.commit();
            }
        });


        getChild();

        return v;

    }


    void checkChild(final DataCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://vaccine-api.000webhostapp.com";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        Log.d("Response", response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            Log.d("message", jsonObj.getString("message"));
                            callback.onSuccess(jsonObj);
                        } catch (final JSONException e) {
                            Log.d("parse error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("method", "getChildList");
                String token = fetch.getTOKEN();
                String regid = fetch.getREG_ID();

                params.put("reg_id", regid);
                params.put("token", token);
                Log.d("data:", regid + " " + " " + token);
                return params;
            }
        };
        queue.add(postRequest);

    }

    public void getChild() {

        checkChild(new DataCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    msg = result.getString("message");
                    status = result.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        Log.d("final status", status);
                        if (result.has("data")) {
                            list.setVisibility(View.VISIBLE);

                            JSONArray arr = result.getJSONArray("data");
                            Log.d("data parse", arr.toString());
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject arrdata = arr.getJSONObject(i);
                                childid.add(arrdata.getString("child_id"));
                                cname.add(arrdata.getString("name"));
                                Log.d("child array id", childid.get(i));
                                Log.d("child array name", cname.get(i));

                            }
                            //---------------------------------------------------------
                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            final GetChildListAdapter adapter = new GetChildListAdapter(inflater, cname, childid);
                            //---------------------------------------------------------------------------------------

                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener()

                            {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(view.getContext(), String.valueOf(parent.getAdapter().getItem(position)), Toast.LENGTH_SHORT).show();
                                    pos = (int) adapter.getItemId(position);
//                                Toast.makeText(view.getContext(),"poition is"+pos,Toast.LENGTH_LONG).show();
//                                Toast.makeText(view.getContext(),"child id :"+childid.get(pos),Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(view.getContext(), Vaccine_chart.class);
                                    i.putExtra("child_id", childid.get(pos));

                                    startActivity(i);

                                }

                            });


                        } else {

                        }
                    } else {
                        Log.d("mesege", msg);
                    }
                } catch (JSONException e) {
                    Log.e("parse error", e.getMessage());
                }
            }
        });
    }


}
