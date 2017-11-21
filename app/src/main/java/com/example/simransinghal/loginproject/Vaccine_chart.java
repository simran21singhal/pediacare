package com.example.simransinghal.loginproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.SharedPrefrences;

public class Vaccine_chart extends AppCompatActivity {


    String msg, status = "false";
    String cid;
    Button scan;

    List<String> vacc = new ArrayList<String>();
    List<String> given_on = new ArrayList<String>();
    List<String> due_on = new ArrayList<String>();
    List<String> flag = new ArrayList<String>();

    ListView list;

    final Activity activity = this;
    String jsonString;
    Map<String, String> map = new HashMap<>();


    private SharedPrefrences fetch;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_chart);

        fetch = new SharedPrefrences(this);
        list = (ListView) findViewById(R.id.lv_list);
        scan = (Button) findViewById(R.id.scan);

        Intent intent = getIntent();
        cid = intent.getStringExtra("child_id");
        getVaccObj();

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        scan.setVisibility(View.INVISIBLE);


    }

    void checkVacObj(final DataCallback callback, final String method, final String given_on, final String due_date, final String vaccine_id) {

        RequestQueue queue = Volley.newRequestQueue(Vaccine_chart.this);
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
                        Toast.makeText(Vaccine_chart.this, "Something went wrong. please try again.", Toast.LENGTH_SHORT).show();
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
                params.put("child_id", cid);
                if(vaccine_id.length() != 0){
                    params.put("vaccine_id", vaccine_id);
                }
                if(given_on.length() != 0){
                    params.put("given_on", given_on);
                }
                if(due_date.length() != 0){
                    params.put("due_date", due_date);
                }
                params.put("all","true");
                Log.d("data:", "reg_id: "+regid + " child_id:" + cid + " token: " + token);
                return params;
            }
        };
        queue.add(postRequest);

    }

    public void getVaccObj() {
        String method = "getChildVaccineList";
        String given_date = "";
        String due_date = "";
        String vaccine_id = "";
        checkVacObj(new DataCallback() {
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
                            vacc.add(arrdata.getString("name"));
                            given_on.add(arrdata.getString("given_on"));
                            due_on.add(arrdata.getString("due_date"));
                            flag.add(arrdata.getString("flag"));

                        }
                        VaccListAdapter adapter = new VaccListAdapter(getLayoutInflater(), vacc, given_on, due_on);
                        list.setAdapter(adapter);

                    }
                    if (status.equalsIgnoreCase("true")) {
                        for(String f: flag){
                            if(f.equalsIgnoreCase("0")){
                                scan.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                        Log.d("final status", status);
                    } else {
                        Log.d("mesege", msg);
                    }
                } catch (JSONException e) {
                    Log.e("parse error", e.getMessage());
                }

            }
        }, method, given_date, due_date, vaccine_id);
    }


    //**********************Scan****************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents()==null){
                Toast.makeText(this,"you cancelled the scanning",Toast.LENGTH_LONG).show();
            }
            else {
                jsonString = result.getContents();

                JSONObject temp = verifyJSON(jsonString);

                if(temp != null){
                    boolean flag = true;
                    if (temp.has("vaccine_id")){
                        map.put("vaccine_id",temp.optString("vaccine_id"));
                    }else{
                        flag = false;
                    }
                    if (temp.has("child_id")){
                        map.put("child_id",temp.optString("child_id"));
                    }else{
                        flag = false;
                    }
                    if (temp.has("given_on")){
                        map.put("given_on",temp.optString("given_on"));
                    }else{
                        flag = false;
                    }
                    if (temp.has("due_date")){
                        map.put("due_date",temp.optString("due_date"));
                    }else{
                        flag = false;
                    }
                    if(flag){
                        //Volley request for qr code
                        progress = new ProgressDialog(Vaccine_chart.this);
                        progress.setMessage("Loading");
                        progress.setCancelable(false);
                        progress.show();
                        updateVaccineChart();
                    }else{
                        Toast.makeText(Vaccine_chart.this, "Something went wrong.QR code error.", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Vaccine_chart.this, "Something went wrong.QR code error.", Toast.LENGTH_SHORT).show();
                }

               // Toast.makeText(this,"child_id: "+map.get("child_id")+" vaccine_id: "+map.get("vaccine_id")+" given date: "+map.get("given_on")+" due Date: "+map.get("due_date"),Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    JSONObject verifyJSON(String para){
        JSONObject temp;
        try {
            temp = new JSONObject(para);
        } catch (JSONException e) {
            temp = null;
            e.printStackTrace();

        }
        return temp;

    }

    public void updateVaccineChart() {
        String method = "updateVaccineChart";
        String given_date = map.get("given_on");
        String due_date = map.get("due_date");
        String vaccine_id = map.get("vaccine_id");
        cid = map.get("child_id");
        Log.d("update date: ","method: "+method+" given: "+given_date+"due: "+due_date+" vaccine_id:"+vaccine_id+" child_id: "+cid);
        checkVacObj(new DataCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    msg = result.getString("message");
                    status = result.getString("status");
                    progress.dismiss();
                    //Toast
                    Toast.makeText(Vaccine_chart.this, msg, Toast.LENGTH_LONG).show();
                    if (status.equalsIgnoreCase("true")) {
                        Log.d("final status", status);
                        //Intent for child whose vaccine chart is updated
                        Intent i = new Intent(Vaccine_chart.this,Vaccine_chart.class);
                        i.putExtra("child_id",cid);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        finish();
                        startActivity(i);

                    } else {
                        Log.d("mesege", msg);
                    }
                } catch (JSONException e) {
                    Log.e("parse error", e.getMessage());
                }

            }
        }, method, given_date, due_date, vaccine_id);
    }


    //*****************scan end************************


}





