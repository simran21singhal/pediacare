package com.example.simransinghal.loginproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mxn.soul.flowingdrawer_core.ElasticDrawer;


import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import utils.SharedPrefrences;

public class ParentActivity extends AppCompatActivity {
    Fragment fragment;
    FragmentManager fragmentManager;
    private SharedPrefrences session1;
    private SharedPrefrences fetch;

    ProgressDialog progress;


    DrawerLayout drawerLayout;
    Boolean mSlideState = false, flag = false, doubleBackToExitPressedOnce = false;
    ListView listView;
    String parent_id, nav_arr[];
    ImageView nav;
    Toolbar toolbar;
    TextView pid;

    Bundle dummy_bundle;

    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        fetch = new SharedPrefrences(this);

        dummy_bundle = savedInstanceState;
//---------------------------------------------------

        alertDialog = new AlertDialog.Builder(ParentActivity.this);
//************************************
        fragmentManager = getSupportFragmentManager();

        if (dummy_bundle == null) {
            fragment = new Parent();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.fragment_replace, fragment, "parent");
            ft.commit();

        }
        //************************************************

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);
        nav_arr = getResources().getStringArray(R.array.nav_menu);
        toolbar = (Toolbar) findViewById(R.id.in);
        pid = (TextView) findViewById(R.id.nav_p_id);

        nav = (ImageView) findViewById(R.id.iv_nav);

        parent_id = fetch.getREG_ID();
        parent_id = "Parent ID : " + parent_id;


        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nav_arr));
        pid.setText(parent_id);


        listView.setOnItemClickListener(new DrawerItemClickListener());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout.addDrawerListener(new ActionBarDrawerToggle(ParentActivity.this, drawerLayout, toolbar,
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
        //-------------------------------
        alertDialog.setTitle("CONFIRM EXIT");
        alertDialog.setMessage("Are you sure you want EXIT?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                System.exit(2);
//                finish();
                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
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

//            selectItem(position);
//            Toast.makeText(ParentActivity.this, planets[position] + "  was clicked", Toast.LENGTH_SHORT).show();


            switch (position) {

                case 0:
                    drawerLayout.closeDrawer(Gravity.START);
                    //**************************
                    fragment = fragmentManager.findFragmentByTag("parent");
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    if (fragment != null) {
                        fragmentTransaction.remove(fragment);
                        fragmentManager.popBackStackImmediate();

                    }
                    fragment = new ChildProfile();
                    fragmentTransaction.add(R.id.fragment_replace, fragment, "profile");
                    fragmentTransaction.addToBackStack("profile");
                    fragmentTransaction.commit();

                    //*************************************

                    break;

                case 1:
                    drawerLayout.closeDrawer(Gravity.START);
                    //**************************
                    fragment = fragmentManager.findFragmentByTag("parent");
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (fragment != null) {
                        ft.remove(fragment);
                        fragmentManager.popBackStackImmediate();

                    }
                    fragment = new DueNavBar();
                    ft.add(R.id.fragment_replace, fragment, "duefrag");
                    ft.addToBackStack("duefrag");
                    ft.commit();

                    //*************************************

                    break;

                case 2:
                    drawerLayout.closeDrawer(Gravity.START);
                    //**************************
                    fragment = fragmentManager.findFragmentByTag("parent");
                    FragmentTransaction ft1 = fragmentManager.beginTransaction();
                    if (fragment != null) {
                        ft1.remove(fragment);
                        fragmentManager.popBackStackImmediate();


                    }
                    fragment = new RecentNavBar();
                    ft1.add(R.id.fragment_replace, fragment, "recentfrag");
                    ft1.addToBackStack("recentfrag");
                    ft1.commit();

                    //*************************************

                    break;


                case 3:
                    logout();
                    break;

            }
        }

    }


//    public void selectItem(int position) {
//        listView.setItemChecked(position, true);
//        setTitle(planets[position]);
//    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    void logout() {
        session1 = new SharedPrefrences(ParentActivity.this);
        progress = new ProgressDialog(ParentActivity.this);
        progress.setMessage("LogOut");
        progress.setCancelable(false);
        progress.show();
        session1.setLogin(false);
        fragmentManager.popBackStack();
        Intent i = new Intent(ParentActivity.this, SignIn.class);
        startActivity(i);
        progress.dismiss();
        finish();

    }


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
