package com.h0pkins3.familymap.userInterface.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.h0pkins3.familymap.R;
import com.h0pkins3.familymap.userInterface.activities.fragments.MyMapFragment;

/** EventActivity
 * Contains all functions with the Event Activity, and uses the Map Fragment to display
 */
public class EventActivity extends AppCompatActivity {

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String arguments = getIntent().getExtras().getString("Event");

        FragmentManager fm = getSupportFragmentManager();
        Fragment mapFragment = new MyMapFragment(arguments);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.map_fragment, mapFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }

    //--****************-- Overriding the up Button --***************--
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
