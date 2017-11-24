package com.example.simransinghal.loginproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import utils.SharedPrefrences;

public class Inventory_Activity extends AppCompatActivity {
    Fragment fragment;
    FragmentManager fragmentManager;
    private SharedPrefrences session1;

    ProgressDialog progress;


    DrawerLayout drawerLayout;
    Boolean mSlideState = false;
    ListView listView;
    String inv_arr[];
    ImageView nav;
    Toolbar toolbar;

    Bundle dummy_bundle;

    AlertDialog.Builder alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        dummy_bundle = savedInstanceState;

        alertDialog = new AlertDialog.Builder(Inventory_Activity.this);

//************************************
        fragmentManager = getSupportFragmentManager();

        if (dummy_bundle == null) {
            fragment = new Inventory();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.fragment_replace, fragment, "inventory");
            ft.commit();
        }
//************************************************

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);
        inv_arr = getResources().getStringArray(R.array.inv_menu);
        toolbar = (Toolbar) findViewById(R.id.in);

        nav = (ImageView) findViewById(R.id.iv_nav);


        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, inv_arr));


        listView.setOnItemClickListener(new Inventory_Activity.DrawerItemClickListener());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout.addDrawerListener(new ActionBarDrawerToggle(Inventory_Activity.this, drawerLayout, toolbar,
                0,
                0) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState = false;//is Closed
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideState = true;//is Opened
            }
        });


        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSlideState) {
                    drawerLayout.closeDrawer(Gravity.START);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }


            }
        });

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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            switch (position) {

                case 0:
                    drawerLayout.closeDrawer(Gravity.START);
                    //**************************
                    fragment = fragmentManager.findFragmentByTag("inventory");
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    if (fragment != null) {
                        fragmentTransaction.remove(fragment);
                    }
                    fragment = new Consumption();
                    fragmentTransaction.add(R.id.fragment_replace, fragment, "consumption");
                    fragmentTransaction.addToBackStack("consumption");
                    fragmentTransaction.commit();

                    //*************************************

                    break;

                case 1:

                    logout();
                    break;

            }
        }

    }


    void logout() {
        session1 = new SharedPrefrences(Inventory_Activity.this);
        progress = new ProgressDialog(Inventory_Activity.this);
        progress.setMessage("LogOut");
        progress.setCancelable(false);
        progress.show();
        session1.setLogin(false);
        fragmentManager.popBackStack();
        Intent i = new Intent(Inventory_Activity.this, SignIn.class);
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
