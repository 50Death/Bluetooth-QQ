package com.lyc.bluetooth3;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    public Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment[] fragments;
    private int lastfragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    //mTextMessage.setText("???");

                    if (lastfragment != 0) {
                        switchFragment(lastfragment,0);
                        lastfragment=0;
                    }
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    if (lastfragment != 1) {
                        switchFragment(lastfragment,1);
                        lastfragment=1;
                    }
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    if (lastfragment != 2) {
                        switchFragment(lastfragment,2);
                        lastfragment=2;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initUI();
        initFragment();
    }

    private void initUI() {
        bottomNavigationView = findViewById(R.id.navigation);
    }

    private void initFragment() {
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragments = new Fragment[]{fragment1, fragment2, fragment3};
        lastfragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).show(fragment1).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);
        if(fragments[index].isAdded()==false){
            transaction.add(R.id.container,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }




}
