package com.example.simransinghal.loginproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Simran Singhal on 24-11-2017.
 */

public class RecentNavBar extends Fragment {
    Fragment fragment;
    FragmentManager fragmentManager;




    String msg, status = "false";

    List<String> vacc = new ArrayList<String>();
    List<String> child = new ArrayList<String>();
    List<String> given_on = new ArrayList<String>();

    ListView list;

    String jsonString;
    Map<String, String> map = new HashMap<>();


    private SharedPrefrences fetch;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recent_vacc_list, container, false);

        fetch = new SharedPrefrences(getContext());
        list = (ListView) v.findViewById(R.id.lv_list);

        getDueVaccObj();



        return v;
    }

    void checkDueVacObj(final DataCallback callback, final String method) {

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
                        progress.dismiss();
                        Toast.makeText(getContext(), "Something went wrong. please try again.", Toast.LENGTH_SHORT).show();
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("method", method);
                String token = fetch.getTOKEN();
                String regid = fetch.getREG_ID();
                params.put("reg_id", regid);
                params.put("token", token);
                return params;
            }
        };
        queue.add(postRequest);

    }

    public void getDueVaccObj() {
        String method = "getRecentChildVaccines";

        checkDueVacObj(new DataCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    msg = result.getString("message");
                    status = result.getString("status");
                    if (result.has("data")) {

                        JSONArray arr = result.getJSONArray("data");
                        Log.d("data parse", arr.toString());
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject arrdata = arr.getJSONObject(i);
                            vacc.add(arrdata.getString("vaccine_name"));
                            child.add(arrdata.getString("child_name"));
                            given_on.add(arrdata.getString("given_on"));

                        }

                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        DueAdapter adapter = new DueAdapter(inflater, vacc, child, given_on);
                        list.setAdapter(adapter);


                    }
                    if (status.equalsIgnoreCase("true")) {

                        Log.d("final status", status);
                    } else {
                        Log.d("mesege", msg);
                    }
                } catch (JSONException e) {
                    Log.e("parse error", e.getMessage());
                }

            }
        }, method);
    }




}
