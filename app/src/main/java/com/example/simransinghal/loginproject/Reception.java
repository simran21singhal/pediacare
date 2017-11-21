package com.example.simransinghal.loginproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Reception extends Fragment implements AdapterView.OnItemSelectedListener {


    String msg, status = "false";


    private SharedPrefrences fetch;

    //***********************
    Fragment fragment;
    FragmentManager fragmentManager;
    //************************


    EditText parent;
    Spinner childspinner, vaccspinner;
    Button child, vaccine, proceed;
    int counter = 0;

    String parent_id;
    int spos1, spos2;
    List<String> child_name_list = new ArrayList<String>();
    List<String> child_id_list = new ArrayList<String>();

    List<String> vacc_name_list = new ArrayList<String>();
    List<String> vacc_id_list = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reception, container, false);
        fragmentManager = getFragmentManager();

        parent = (EditText) v.findViewById(R.id.parent);
        child = (Button) v.findViewById(R.id.child);

        childspinner = (Spinner) v.findViewById(R.id.childspinner);
        vaccine = (Button) v.findViewById(R.id.vaccine);

        vaccspinner = (Spinner) v.findViewById(R.id.vaccspinner);
        proceed = (Button) v.findViewById(R.id.proceed);

        fetch = new SharedPrefrences(getContext());


        childspinner.setVisibility(View.INVISIBLE);
        vaccspinner.setVisibility(View.INVISIBLE);
        vaccine.setVisibility(View.INVISIBLE);
        proceed.setVisibility(View.INVISIBLE);


        //Onclick get child list button
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent_id = parent.getText().toString();
                getChildList();
                parent.clearFocus();
            }
        });

        //Onclick get vaccine list button
        vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getVaccineList();
                vaccine.setClickable(false);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //************************************

                Bundle bundle = new Bundle();

                bundle.putString("child_id", child_id_list.get(spos1));
                bundle.putString("child_name", child_name_list.get(spos1));
                bundle.putString("vacc_id", vacc_id_list.get(spos2));
                bundle.putString("vacc_name", vacc_name_list.get(spos2));
                //set Fragmentclass Arguments
//                QRcodeGenerate fragobj = new QRcodeGenerate();
//                fragobj.setArguments(bundle);


                Log.d("bundle data", child_id_list.get(spos1));

                fragment = fragmentManager.findFragmentByTag("reception");
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (fragment != null) {
                    fragmentTransaction.remove(fragment);
                }
                fragment = new QRcodeGenerate();
                fragment.setArguments(bundle);
                Log.d("fragment object", fragment.toString());
                fragmentTransaction.add(R.id.fragment_replace, fragment, "generate");
                fragmentTransaction.addToBackStack("generate");



                fragmentTransaction.commit();


                //************************************


//                Intent i = new Intent(Reception.this,QRcodeGenerate.class);
//                i.putExtra("child_id",child_id_list.get(spos1));
//                i.putExtra("child_name",child_name_list.get(spos1));
//                i.putExtra("vacc_id",vacc_id_list.get(spos2));
//                i.putExtra("vacc_name",vacc_name_list.get(spos2));
//
//               // parent.setText(null);
//                startActivity(i);

            }
        });


        //***************On touch***************

        parent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Log.d("focus on code", "inside focus");
                    childspinner.setVisibility(View.INVISIBLE);
                    vaccine.setVisibility(View.INVISIBLE);
                    child.setClickable(true);
                    child_id_list.clear();
                    child_name_list.clear();
                    vacc_id_list.clear();
                    vacc_name_list.clear();

                    vaccspinner.setVisibility(View.INVISIBLE);
                    proceed.setVisibility(View.INVISIBLE);
                }

            }
        });
        return v;

    }



    //***************VOLLEY METHOD***********************
    void hitAPI(final DataCallback callback, final String method, final String child_id, final String all, final String parentid) {

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
                params.put("method", method);
                String token = fetch.getTOKEN();
                String regid = fetch.getREG_ID();
                params.put("reg_id", regid);
                params.put("token", token);
                if (parentid.length() != 0) {
                    params.put("parent_id", parentid);
                }
                if (child_id.length() != 0) {
                    params.put("child_id", child_id);
                }
                if (all.length() != 0) {
                    params.put("all", all);
                }
                Log.d("data:", "reg_id: " + regid + " child_id:" + child_id + " token: " + token + " all: " + all + " parent_id: " + parentid);
                return params;
            }
        };
        queue.add(postRequest);

    }

    //*********************CALLING VOLLEY METHOD FOR CHILD LIST**************************
    public void getChildList() {
        String method = "getChildList";
        String all = "";
        String child_id = "";
        String parentid = parent_id;
        hitAPI(new DataCallback() {
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
                            child_name_list.add(arrdata.getString("name"));
                            child_id_list.add(arrdata.getString("child_id"));

                        }

                    }
                    if (status.equalsIgnoreCase("true")) {
                        Log.d("final status", status);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, child_name_list);
                        childspinner.setAdapter(adapter);
                        childspinner.setOnItemSelectedListener(Reception.this);
                        childspinner.setVisibility(View.VISIBLE);
                        vaccine.setVisibility(View.VISIBLE);
                        child.setClickable(false);
                    } else {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.d("mesege", msg);
                    }
                } catch (JSONException e) {
                    Log.e("parse error", e.getMessage());
                }

            }
        }, method, child_id, all, parentid);
    }

    //*********************CALLING VOLLEY METHOD FOR CHILD LIST**************************
    public void getVaccineList() {
        String method = "getChildVaccineList";
        String all = "false";
        String child_id = child_id_list.get(spos1);
        String parentid = parent_id;
        hitAPI(new DataCallback() {
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
                            vacc_name_list.add(arrdata.getString("name"));
                            vacc_id_list.add(arrdata.getString("vaccine_id"));

                        }

                    }
                    if (status.equalsIgnoreCase("true")) {
                        Log.d("final status", status);

                        ArrayAdapter<String> vacc = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, vacc_name_list);
                        vaccspinner.setAdapter(vacc);
                        vaccspinner.setOnItemSelectedListener(Reception.this);
                        vaccspinner.setVisibility(View.VISIBLE);
                        proceed.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.d("mesege", msg);
                    }
                } catch (JSONException e) {
                    Log.e("parse error", e.getMessage());
                }

            }
        }, method, child_id, all, parentid);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.childspinner) {
            vaccine.setClickable(true);
            parent.getItemAtPosition(position);
            spos1 = position;
            vacc_name_list.clear();
            vacc_id_list.clear();
            vaccspinner.setVisibility(View.INVISIBLE);
            proceed.setVisibility(View.INVISIBLE);
        } else if (spinner.getId() == R.id.vaccspinner) {
            parent.getItemAtPosition(position);
            spos2 = position;
        }

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        fragmentManager.popBackStack();
//        System.exit(0);
//        Log.d("fragmentlyfcycle", "onresume");
//    }

}
