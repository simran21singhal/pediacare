package com.example.simransinghal.loginproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class Parent extends AppCompatActivity {

    ListView list;
    Button addbtn;
    List<String> cname = new ArrayList<String>();
    List<String> childid = new ArrayList<String>();
    int pos;



    String msg, status = "false";
    private SharedPrefrences fetch;


    //***********************************
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            moveTaskToBack(true);
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    //**********************************************************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent);

        fetch = new SharedPrefrences(this);

        list = (ListView) findViewById(R.id.lv_list);
        addbtn = (Button) findViewById(R.id.bt_add);
        addbtn.setVisibility(View.INVISIBLE);
        list.setVisibility(View.INVISIBLE);

        getChild();

    }


    void checkChild(final DataCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(Parent.this);
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
                    if (result.has("data")) {
                        //addbtn.setVisibility(View.INVISIBLE);
                        list.setVisibility(View.VISIBLE);

                        JSONArray arr = result.getJSONArray("data");
                        Log.d("data parse", arr.toString());
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject arrdata = arr.getJSONObject(i);
                            childid.add(arrdata.getString("child_id"));
                            cname.add(arrdata.getString("name"));
                            Log.d("child array id",childid.get(i) );
                            Log.d("child array name",cname.get(i) );

                        }
                        final GetChildListAdapter adapter = new GetChildListAdapter(getLayoutInflater(), cname, childid);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener()

                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(view.getContext(), String.valueOf(parent.getAdapter().getItem(position)), Toast.LENGTH_SHORT).show();
                                 pos = (int) adapter.getItemId(position);
                                Toast.makeText(view.getContext(),"poition is"+pos,Toast.LENGTH_LONG).show();
                                Toast.makeText(view.getContext(),"child id :"+childid.get(pos),Toast.LENGTH_LONG).show();
                                Intent i = new Intent(view.getContext(), Vaccine_chart.class);
                                i.putExtra("child_id",childid.get(pos) );

                                startActivity(i);

                            }

                        });


                    } else {
                        addbtn.setVisibility(View.VISIBLE);
                        addbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Parent.this,ChildProfile.class);
                                startActivity(i);
                            }
                        });
//                        list.setVisibility(View.INVISIBLE);
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
        });
    }


}
