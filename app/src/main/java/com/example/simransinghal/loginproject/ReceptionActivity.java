package com.example.simransinghal.loginproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);

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

    }

//        toolbar.setOnMenuItemClickListener(
//                new Toolbar.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        // Handle menu item click event
//                        int id = item.getItemId();
//                        switch (id) {
//                            case R.id.logout:
//                                Toast.makeText(ReceptionActivity.this, "Logout", Toast.LENGTH_SHORT).show();
//                                logout();
//                        }
//                        return true;
//                    }
//                });



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
    boolean doubleBackToExitPressedOnce = false;
int c=0;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ++c;

//if(c==2)
//    System.exit(0);

        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(ReceptionActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        Log.d("val of c",Integer.toString(c));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.d("val of c in run",Integer.toString(c));

                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
   }
}
