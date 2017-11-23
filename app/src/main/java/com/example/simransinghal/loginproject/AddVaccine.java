package com.example.simransinghal.loginproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utils.SharedPrefrences;

/**
 * Created by Simran Singhal on 22-11-2017.
 */

public class AddVaccine extends Fragment {

    Fragment fragment;
    FragmentManager fragmentManager;

    boolean flag = true;


    String vname,stock,duration;
    String status = "false", msg;
    ProgressDialog progress;
    private SharedPrefrences fetch;
    EditText et_stock;
    EditText vaccinename,tv_duration;
    Button addvacc;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.updatevaccine, container, false);


        vaccinename = v.findViewById(R.id.vaccine_name);
        tv_duration = v.findViewById(R.id.duration);
        et_stock = v.findViewById(R.id.stock);
        addvacc = v.findViewById(R.id.update);
        fetch = new SharedPrefrences(getContext());




        addvacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vname = vaccinename.getText().toString();
                stock = et_stock.getText().toString();
                duration = tv_duration.getText().toString();

                if (vaccinename.length() == 0) {
                  vaccinename.setError("Vaccine Name is required");
                    vaccinename.requestFocus();
                    flag = false;
                }
                if (et_stock.length() == 0) {
                    et_stock.setError("Stock is required");
                    et_stock.requestFocus();
                    flag = false;
                }else{
                    if(!stock.trim().matches("^[0-9]\\d*$")){
                        et_stock.setError("Stock must be a number.");
                        et_stock.requestFocus();
                        flag = false;
                    }
                }
                if (tv_duration.length() == 0) {
                    tv_duration.setError("Duration is required");
                    tv_duration.requestFocus();
                    flag = false;
                }else{
                    if(!duration.trim().matches("^[0-9]\\d*$")){
                        tv_duration.setError("Duration must be a number.");
                        tv_duration.requestFocus();
                        flag = false;
                    }
                }



                if(flag){
                    getResponse();
                }

            }
        });


        return v;
    }


    void addVaccine(final DataCallback callback) {

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
                params.put("method", "addVaccine");

                String regid = fetch.getREG_ID();
                String token = fetch.getTOKEN();
                Log.d("data:", regid + " " + token);

                params.put("reg_id", regid);
                params.put("token", token);
                params.put("name", vname);
                params.put("duration", duration);
                params.put("stock", stock);


                return params;
            }
        };
        queue.add(postRequest);

    }

    void getResponse(){
        addVaccine(new DataCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    msg = result.getString("message");
                    status = result.getString("status");
//                    progress.dismiss();
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                    if (status.equalsIgnoreCase("true")) {

                        fragmentManager = getFragmentManager();

                        fragment = fragmentManager.findFragmentByTag("add_vacc");
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (fragment != null) {
                            fragmentTransaction.remove(fragment);
                        }
                        fragment = new Inventory();
                        fragmentTransaction.add(R.id.fragment_replace, fragment, "inventory");
                        //fragmentTransaction.addToBackStack("inventory");
                        fragmentTransaction.commit();

                    }

                    else {
                        Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT).show();

                    }
                } catch(JSONException e){
                    Log.e("parse error", e.getMessage());
                }
            }
        });
    }
}