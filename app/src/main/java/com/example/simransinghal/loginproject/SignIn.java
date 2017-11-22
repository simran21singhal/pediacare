package com.example.simransinghal.loginproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import utils.SharedPrefrences;

public class SignIn extends AppCompatActivity {
    Button button;
    EditText uname, pass;
    String status = "false";
    String name, password;
    String regid;
    String tokenid, type, msg;
    private SharedPrefrences session;
    private SharedPrefrences transfer;

    ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        transfer = new SharedPrefrences(this);
        session = new SharedPrefrences(this);



        button = (Button) findViewById(R.id.button);
        uname = (EditText) findViewById(R.id.editText2);
        pass = (EditText) findViewById(R.id.editText4);

        session.setLogin(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = new ProgressDialog(SignIn.this);
                progress.setMessage("Loading");
                progress.setCancelable(false);
                progress.show();
                attemptLogin();

            }
        });
        if (session.isLoggedIn()) {
            //Intent intent=new Intent(this,Parent.class);

            //startActivity(intent);

            // finish();
        }
    }


    void checkUser(final DataCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(SignIn.this);
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
//                            setStatus(jsonObj.getString("status"));


//                            Log.d("response",msg+" "+regid+" "+tokenid+" "+type);

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
                        progress.dismiss();
                        Toast.makeText(SignIn.this, "Something went wong, Please try again.", Toast.LENGTH_SHORT).show();
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("method", "login");


                params.put("email", name);
                params.put("password", password);
                Log.d("data:", name + " " + " " + password);
                return params;
            }
        };
        queue.add(postRequest);

    }


    void attemptLogin() {
        init();

        checkUser(new DataCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    msg = result.getString("message");
                    status = result.getString("status");

                    Log.d("raw: ", result.toString());
                    progress.dismiss();
                    Toast.makeText(SignIn.this, msg, Toast.LENGTH_SHORT).show();
                    if (result.has("data")) {
                        JSONObject data = result.getJSONObject("data");
                        regid = data.getString("reg_id");
                        transfer.putRegId(regid);
                        //regid = Integer.parseInt(id);
                        tokenid = data.getString("token");
                        transfer.putToken(tokenid);
                        type = data.getString("type");
                    }
                    if (status.equalsIgnoreCase("true")) {
                        tologin();

                        Log.d("final data:", name + " " + " " + password);
                    } else {


                    }
                } catch (JSONException e) {
                    Log.e("parse error", e.getMessage());
                }
            }
        });
    }


    void init() {
        name = uname.getText().toString();
        password = pass.getText().toString();

    }

    void tologin() {
        if(type.equalsIgnoreCase("Parent")) {
            Intent i = new Intent(SignIn.this, ParentActivity.class);
            session.setLogin(true);

            startActivity(i);

        }
        else if(type.equalsIgnoreCase("Reception")) {

            Intent i = new Intent(SignIn.this, ReceptionActivity.class);
            session.setLogin(true);

            startActivity(i);

        }
        else if(type.equalsIgnoreCase("Inventory")) {
            Toast.makeText(this, "akad bakad", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(SignIn.this, Inventory_Activity.class);
            session.setLogin(true);

            startActivity(i);
        }
        //***********


        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Log.d("i am here","pagal");
//        //finish();
//
//        System.exit(0);
//    }
}
