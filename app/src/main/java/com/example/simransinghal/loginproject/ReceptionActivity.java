package com.example.simransinghal.loginproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import utils.SharedPrefrences;

public class ReceptionActivity extends AppCompatActivity {
    Fragment fragment;
    FragmentManager fragmentManager;
    private SharedPrefrences session1;
    ProgressDialog progress;
    Toolbar toolbar;

    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);

        alertDialog = new AlertDialog.Builder(ReceptionActivity.this);


        toolbar = (Toolbar) findViewById(R.id.in);


        //Log.d("tooolbar", toolbar.toString());

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragment = new Reception();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.fragment_replace, fragment, "reception");
            ft.commit();

        }
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayShowTitleEnabled(false);


        alertDialog.setTitle("CONFIRM EXIT");
        alertDialog.setMessage("Are you sure you want EXIT?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
//                System.exit(2);
                finish();
//                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        Log.d("welcome","inflator");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                Toast.makeText(ReceptionActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                logout();
        }
        return true;
    }

    void logout() {
        session1 = new SharedPrefrences(ReceptionActivity.this);
        progress = new ProgressDialog(ReceptionActivity.this);
        progress.setMessage("LogOut");
        progress.setCancelable(false);
        progress.show();
        session1.setLogin(false);
        fragmentManager.popBackStack();
        Intent i = new Intent(ReceptionActivity.this,SignIn.class);
        startActivity(i);
        finish();

    }

    //***************back button exit

        @Override
        public void onBackPressed() {

            if(getSupportFragmentManager().getBackStackEntryCount()==0)
            {
                alertDialog.show();
            }
            else
            {
                super.onBackPressed();
                getSupportFragmentManager().popBackStack();
            }


        }
}
