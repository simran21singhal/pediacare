package com.example.simransinghal.loginproject;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import utils.SharedPrefrences;

/**
 * Created by Simran Singhal on 22-11-2017.
 */

public class Consumption extends Fragment {

    Fragment fragment;
    FragmentManager fragmentManager;
    Button btnfetch;
    TextView datepicker;
    String msg, status = "false", datePick;
    List<String> vname = new ArrayList<String>();
    List<String> count = new ArrayList<String>();
    List<Integer> sno = new ArrayList<Integer>();
    private SharedPrefrences fetch;
    ListView listView;
    LinearLayout ll;



    //**********DATEPICKER*******************
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            view.setMaxDate(System.currentTimeMillis());
            updateLabel();
        }

    };


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datepicker.setText(sdf.format(myCalendar.getTime()));
    }
    //***********************

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.consumption, container, false);

        btnfetch = v.findViewById(R.id.btn_fetch);
        datepicker = v.findViewById(R.id.et_date);
        fetch = new SharedPrefrences(getContext());


        listView = (ListView) v.findViewById(R.id.list);

        ll = v.findViewById(R.id.ll);
        ll.setVisibility(View.INVISIBLE);




        datepicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog mDate =  new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mDate.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDate.show();
            }
        });

        btnfetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vname.clear();
                sno.clear();
                count.clear();
                if(datepicker.length() == 0){
                    datepicker.setError("Date is required");
                }else{
                    datePick = datepicker.getText().toString();
                    datepicker.setError(null);
                    getResponse();
                }
            }
        });



        return v;
    }

    void fetchRecords(final DataCallback callback) {

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
                params.put("method", "getConsumptionByDate");

                String regid = fetch.getREG_ID();
                String token = fetch.getTOKEN();


                params.put("reg_id", regid);
                params.put("token", token);
                params.put("date", datePick);
                Log.d("data:", regid + " " + token+" date:"+datePick);

                return params;
            }
        };
        queue.add(postRequest);

    }

    void getResponse(){
        fetchRecords(new DataCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    msg = result.getString("message");
                    status = result.getString("status");
//                    progress.dismiss();
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                    if (status.equalsIgnoreCase("true")) {

                        if (result.has("data")) {
                            ll.setVisibility(View.VISIBLE);

                            JSONArray arr = result.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject arrdata = arr.getJSONObject(i);
                                vname.add(arrdata.getString("name"));
                                count.add(arrdata.getString("count"));
                                sno.add(i+1);
                                Log.d("vacc array name", vname.get(i));
                                Log.d("count array ", count.get(i));
                            }
                            Log.d("list fetch", "complete");
                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final GetVaccAccDate adapter = new GetVaccAccDate(inflater,vname,count,sno);

                            //---------------------------------------------------------------------------------------

                            listView.setAdapter(adapter);
                        }



                    }

                    else {

                    }
                } catch(JSONException e){
                    Log.e("parse error", e.getMessage());
                }
            }
        });
    }
}
