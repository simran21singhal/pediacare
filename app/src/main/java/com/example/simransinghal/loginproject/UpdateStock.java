package com.example.simransinghal.loginproject;

import android.app.ProgressDialog;
import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utils.SharedPrefrences;

/**
 * Created by Simran Singhal on 22-11-2017.
 */

public class UpdateStock extends Fragment {

    String vname,vid,stock,duration;
    String status = "false", msg;
    ProgressDialog progress;
    private SharedPrefrences fetch;
    EditText et_stock;
    TextView vaccinename,tv_duration;
    Button update;

    Fragment fragment;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.update_stock, container, false);

        vaccinename = v.findViewById(R.id.vaccine_name);
        tv_duration = v.findViewById(R.id.duration);
        et_stock = v.findViewById(R.id.stock);
        update = v.findViewById(R.id.update);
        fetch = new SharedPrefrences(getContext());

        Bundle bundle = this.getArguments();
        Log.d("bundle di maaa di",bundle.toString());
        if (bundle != null) {
             vname = bundle.getString("vname");
             vid = bundle.getString("vid");
             stock = bundle.getString("stock");
             duration = bundle.getString("duration");
        }

        tv_duration.setText(duration);
        vaccinename.setText(vname);
        et_stock.setHint(stock);
//        et_stock.setHint(Integer.parseInt(stock));


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stock=et_stock.getText().toString();

                getResponse();

            }
        });


        return v;
    }

    void updateVaccineStock(final DataCallback callback) {

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
                params.put("method", "updateVaccine");

                String regid = fetch.getREG_ID();
                String token = fetch.getTOKEN();
                Log.d("data:", regid + " " + token);

                params.put("reg_id", regid);
                params.put("token", token);
                params.put("vaccine_id", vid);
                params.put("name", vname);
                params.put("duration", duration);
                params.put("stock", stock);


                return params;
            }
        };
        queue.add(postRequest);

    }

    void getResponse(){
        updateVaccineStock(new DataCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    msg = result.getString("message");
                    status = result.getString("status");
//                    progress.dismiss();
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                    if (status.equalsIgnoreCase("true")) {

                        fragmentManager = getFragmentManager();

                        fragment = fragmentManager.findFragmentByTag("stock");
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
