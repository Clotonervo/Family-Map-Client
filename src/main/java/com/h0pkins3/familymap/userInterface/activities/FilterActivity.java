package com.h0pkins3.familymap.userInterface.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.h0pkins3.familymap.R;
import com.h0pkins3.familymap.models.Model;
import com.h0pkins3.familymap.userInterface.listStuff.FilterHolder;
import com.h0pkins3.familymap.models.Filter;
import com.h0pkins3.familymap.userInterface.listStuff.FilterRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

/** FilterActivity
 * Contains all information for the Filter Activity, and uses a recycler view and a filter Adapter
 */
public class FilterActivity extends AppCompatActivity {

    private RecyclerView mFilterRecycler;
    private FilterRecycleAdapter mFilterAdapter;

    private Model model = Model.initialize();

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFilterRecycler = findViewById(R.id.filter_recycler);
        mFilterRecycler.setLayoutManager(new LinearLayoutManager(this));

        updateUI();
    }

    //--****************-- Initializing the Filter Adapter --***************--
    private void updateUI()
    {
        List<String> defaultFilter = new ArrayList<>();
        defaultFilter.add("Father's Side");
        defaultFilter.add("Mother's Side");
        defaultFilter.add("Male Events");
        defaultFilter.add("Female Events");

        List<String> eventTypes = model.getEventTypes();
        defaultFilter.addAll(eventTypes);
        mFilterAdapter = new FilterRecycleAdapter(defaultFilter, this);
        mFilterRecycler.setAdapter(mFilterAdapter);
    }

    //--****************-- Overriding the up Button and creating the Options Menu --***************--
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

}
