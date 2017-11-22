package com.example.simransinghal.loginproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import static com.example.simransinghal.loginproject.R.layout.signup;

/**
 * Created by Simran Singhal on 21-11-2017.
 */

public class Inventory extends Fragment {

    String status = "false", msg;
    ProgressDialog progress;
    private SharedPrefrences fetch;
    ListView list;
    int pos;
    Fragment fragment;
    FragmentManager fragmentManager;



    List<String> vname = new ArrayList<String>();
    List<String> vid = new ArrayList<String>();
    List<String> stock = new ArrayList<String>();
    List<String> duration = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.inventory, container, false);

        fetch = new SharedPrefrences(getContext());

         list = v.findViewById(R.id.list);
        fragmentManager = getFragmentManager();



        //********************FLOATING BUTTON*********************************
        ImageView fab = (ImageView) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action


            }
        });
        //************************************************************

        return v;

    }


    void getVaccList(final DataCallback callback) {

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
//                              setStatus(jsonObj.getString("status"));
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
                       // progress.dismiss();
                        Toast.makeText(getContext(), "Something went wong, Please try again.", Toast.LENGTH_SHORT).show();
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("method", "getVaccineList");

                String regid = fetch.getREG_ID();
                String token = fetch.getTOKEN();
                Log.d("data:", regid + " " + token);

                params.put("reg_id", regid);
                params.put("token", token);


                return params;
            }
        };
        queue.add(postRequest);

    }

    @Override
    public void onResume() {
        super.onResume();


        vname.clear();
        vid.clear();
        duration.clear();
        stock.clear();
        getVaccList(new DataCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    msg = result.getString("message");
                    status = result.getString("status");
//                    progress.dismiss();
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        if (status.equalsIgnoreCase("true")) {
                            Log.d("final status", status);
                            if (result.has("data")) {

                                JSONArray arr = result.getJSONArray("data");
                                Log.d("data parse", arr.toString());
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject arrdata = arr.getJSONObject(i);
                                    vid.add(arrdata.getString("id"));
                                    vname.add(arrdata.getString("name"));
                                    stock.add(arrdata.getString("stock"));
                                    duration.add(arrdata.getString("duration"));

                                    Log.d("vacc array id", vid.get(i));
                                    Log.d("vacc array name", vname.get(i));
                                    Log.d("stockk array ", stock.get(i));


                                }
                                //---------------------------------------------------------
                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                Context cont;
//                                cont=getActivity();
                                final GetVacForInvAdapter adapter = new GetVacForInvAdapter(inflater, vname,vid,stock);

                                //---------------------------------------------------------------------------------------

                                list.setAdapter(adapter);
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        pos = (int) adapter.getItemId(position);

                                        Bundle bundle = new Bundle();
                                        bundle.putString("vname", vname.get(position));
                                        bundle.putString("vid", vid.get(position));
                                        bundle.putString("stock", stock.get(position));
                                        bundle.putString("duration", duration.get(position));

                                        Log.d("bundle data",vname.get(position)+" "+vid.get(position));


                                        fragment = fragmentManager.findFragmentByTag("inventory");
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        if (fragment != null) {
                                            fragmentTransaction.remove(fragment);
                                        }
                                        fragment = new UpdateStock();
                                        fragmentTransaction.add(R.id.fragment_replace, fragment, "stock");
                                        fragmentTransaction.addToBackStack("stock");

                                        fragment.setArguments(bundle);

                                        fragmentTransaction.commit();

                                    }

                                });

                            }

                        } else {
                            Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch(JSONException e){
                        Log.e("parse error", e.getMessage());
                    }
                }
            });
        }



    }
