package com.example.simransinghal.loginproject;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TANVIPC on 02-11-2017.
 */

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button signup;
    TextView signin;
    EditText email, pass, pass1, uname;
//    private ProgressDialog progressDialog;

    Spinner spinner;
    String type[] = {"Select type of Login", "Inventory", "Reception", "Parent"};
    int pos;
    String tname;
     String status="false";
    String uuname,password1,password,mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

//        progressDialog=new ProgressDialog(this);
//        progressDialog.setTitle("Loading.....");
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(true);


        signup = (Button) findViewById(R.id.button);
        signin = (TextView) findViewById(R.id.textView3);
        uname = (EditText) findViewById(R.id.name);

        email = (EditText) findViewById(R.id.editText1);
        pass = (EditText) findViewById(R.id.editText2);
        pass1 = (EditText) findViewById(R.id.editText3);
        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, type);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(SignUp.this);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
              //  progressDialog.show();
                //finish();

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, SignIn.class);
                startActivity(i);
                //finish();

            }
        });


    }


    //call it when u post data to firebase
    //getter setter are in Register.java
    public void validation() {
        init();


         if (uuname.length() == 0) {
            uname.setError("Username is Required");
            uname.requestFocus();
        }
        else if (pos == 0) {
            Toast.makeText(SignUp.this, "select type of login", Toast.LENGTH_SHORT).show();
        }
         else if (mail.length() == 0) {
            email.setError("Email is Required");
            email.requestFocus();
        }
        else if (!constraints(mail)) {
            email.setError("Enter a valid mailid");
            email.requestFocus();

        }
        else if (password.length() == 0) {
            pass.setError("Password not entered");
            pass.requestFocus();
        }
        else if (password1.length() == 0) {
            pass1.setError("Please confirm password");
            pass1.requestFocus();
        }
        else if (!password.equals(password1)) {
            pass1.setError("Password Not matched");
            pass1.requestFocus();
        }
       else  if (password.length() < 8) {
            pass.setError("Password should be atleast of 8 charactors");
            pass.requestFocus();
        }

        else{

             checkUser(new DataCallback() {
                 @Override
                 public void onSuccess(JSONObject result) {
                     try {
                         signup.setClickable(true);
                         if(result.getString("status").equalsIgnoreCase("true")){
                             Intent i = new Intent(SignUp.this,SignIn.class);
                             emptyInputEditText();
                            // progressDialog.dismiss();
                             startActivity(i);
                             finish();

                         }else{
                            // progressDialog.dismiss();

                             Toast.makeText(SignUp.this, "Already Registered", Toast.LENGTH_LONG).show();

//                             Snackbar snackbar = Snackbar
//                                     .make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
//
//                             snackbar.show();
                         }
                     } catch (JSONException e) {
                         Log.e("parse error", e.getMessage());
                     }
                 }
             });
        }


    }

    public boolean constraints(String mail) {

        Pattern emailPattern = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(mail);
        return emailMatcher.matches();

    }

    String getpos(int pos) {
        tname = type[pos];
        return tname;

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
        pos = position;
        tname = getpos(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void checkUser(final DataCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(SignUp.this);
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
                              setStatus(jsonObj.getString("status"));
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
                params.put("method", "registration");

               //init();
                Log.d("data:",uuname+" "+mail+" "+password+" "+tname);

                params.put("name", uuname);
                params.put("email", mail);
                params.put("password", password);
                params.put("type",tname);

                return params;
            }
        };
        queue.add(postRequest);
        signup.setClickable(false);

    }


    private void emptyInputEditText() {
        uname.setText(null);
        pass.setText(null);
        pass1.setText(null);
        email.setText(null);
        spinner.setSelection(0);

    }
void setStatus(String st){
    status = st;

}

    void init()
    {
        uuname = uname.getText().toString();
         mail = email.getText().toString();
        password = pass.getText().toString();
         password1 = pass1.getText().toString();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
