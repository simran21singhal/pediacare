package com.example.simransinghal.loginproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import utils.SharedPrefrences;

public class ChildProfile extends AppCompatActivity {
    RadioButton rb_male, rb_female;
    RadioGroup grp;
    EditText name, datepicker;
    Button bt_add;
    String c_name, dob, gender = null, msg, status;
    private SharedPrefrences fetch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.childprofile);

        grp = (RadioGroup) findViewById(R.id.editText2);
        rb_male = (RadioButton) findViewById(R.id.boy);
        rb_female = (RadioButton) findViewById(R.id.girl);
        datepicker = (EditText) findViewById(R.id.editText3);
        name = (EditText) findViewById(R.id.editText);
        bt_add = (Button) findViewById(R.id.next);

        fetch = new SharedPrefrences(this);


        datepicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog mDate =  new DatePickerDialog(ChildProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mDate.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDate.show();

            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                  bt_add.setClickable(false);
//                bt_add.setVisibility(View.INVISIBLE);
                    getRes();
                }
            }
        });



    }


    //***************Date picker************************

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



//    public static boolean isDateOfInterestValid(String dateformat,
//                                                String currentDate, String dateOfInterest, Boolean todayValid) {
//
//        String format = dateformat;
//        SimpleDateFormat sdfj = new SimpleDateFormat(format);
//        Date cd = null;  // current date
//        Date doi = null; // date of interest
//
//        try {
//            cd = sdfj.parse(currentDate);
//            doi = sdfj.parse(dateOfInterest);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        long diff = doi.getTime() - cd.getTime();
//        int diffDays = (int) (diff / (24 * 1000 * 60 * 60));
//        if(todayValid){
//            if (diffDays >= 0) {
//                return false;
//            } else {
//                return true;
//            }
//        }else{
//            if (diffDays > 0) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//
//    }

    //****************getting values from user*******************
    void get() {
        if (rb_male.isChecked()) {
            gender = "male";
        } else if (rb_female.isChecked()) {
            gender = "female";
        }

        c_name = name.getText().toString();

        dob = datepicker.getText().toString();
    }

    //*************************validate********************************
    boolean validate() {
        get();
        boolean flag=true;

        if (c_name.length() == 0) {
            name.setError("Name is Required");
            name.requestFocus();
            flag=false;
        }
        if (dob.length() == 0) {
            datepicker.setError("Date of birth is Required");
            datepicker.requestFocus();
            flag=false;
        }

        return flag;

    }
    //***************API Method***************


    void addChild(final DataCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(ChildProfile.this);
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
                params.put("method", "addChild");
                String token = fetch.getTOKEN();
                String regid = fetch.getREG_ID();

                params.put("reg_id", regid);
                params.put("token", token);
                params.put("name", c_name);
                params.put("gender", gender);
                params.put("dob", dob);
                Log.d("child data:", regid + " " + c_name + " " + gender + " " + dob + " " + token);
                return params;
            }
        };
        queue.add(postRequest);

    }


    public void getRes() {

        addChild(new DataCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    msg = result.getString("message");
                    status = result.getString("status");


                    if (status.equalsIgnoreCase("true")) {
                        Log.d("final status", status);
                        Toast.makeText(ChildProfile.this, msg, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ChildProfile.this,Parent.class);
                        startActivity(i);
                    } else {
                        Log.d("mesege", msg);
                        bt_add.setClickable(true);
//                        bt_add.setVisibility(View.VISIBLE);
                        Toast.makeText(ChildProfile.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("parse error", e.getMessage());
                }
            }
        });
    }


}
