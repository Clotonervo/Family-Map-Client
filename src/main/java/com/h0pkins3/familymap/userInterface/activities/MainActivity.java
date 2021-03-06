package com.h0pkins3.familymap.userInterface.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.h0pkins3.familymap.R;
import com.h0pkins3.familymap.userInterface.activities.fragments.LoginFragment;
import com.h0pkins3.familymap.userInterface.activities.fragments.MyMapFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener {

    private FragmentManager fm = getSupportFragmentManager();

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if ((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("Re-sync"))){

            Fragment mapFragment = new MyMapFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            fragmentTransaction.add(R.id.fragment_container, mapFragment).commit();
        }
        else if (fragment == null) {
            fragment = new LoginFragment();
            ((LoginFragment) fragment).setLoginListener(this);
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    //--****************-- On Login success --***************--
    @Override
    public void loginComplete()
    {
        Fragment mapFragment = new MyMapFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, mapFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


}
