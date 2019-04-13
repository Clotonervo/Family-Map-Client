package com.h0pkins3.familymap.userInterface.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.h0pkins3.familymap.R;
import com.h0pkins3.familymap.models.Model;
import com.h0pkins3.familymap.models.baseModels.Events;
import com.h0pkins3.familymap.models.baseModels.Persons;
import com.h0pkins3.familymap.userInterface.listStuff.SearchAdapter;
import com.h0pkins3.familymap.userInterface.listStuff.SearchHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** SearchActivity
 * Contains all information about the Search Activity, and initializes the Search Adapater
 */
public class SearchActivity extends AppCompatActivity {

    private EditText mSearchBar;
    private Button mSearchButton;
    private String searchInput;
    private RecyclerView mSearchRecycler;
    private RecyclerView.Adapter mSearchAdapter;

    private Model model = Model.initialize();

    //________________________ onCreate and other Activity functions ____________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchBar = findViewById(R.id.search_text);
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                searchInput = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s)
            {}
        });

        mSearchButton = findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (searchInput != null){
                    updateUI();
                }
            }
        });

        mSearchRecycler = findViewById(R.id.list_search_recycler);
        mSearchRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    //--****************-- Overriding the up Button --***************--
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

    //--****************-- Initializing the Search Adapter --***************--
    private void updateUI()
    {
        List<Object> objectList = new ArrayList<>();

        Map<String, Persons> availablePeople = model.getPeople();
        getPersonsList(availablePeople, objectList);

        Map<String, Events> availableEvents = model.getDisplayedEvents();
        getEventsList(availableEvents, objectList);

        if (objectList.size() != 0) {
            mSearchAdapter = new SearchAdapter(objectList, this);
            mSearchRecycler.setAdapter(mSearchAdapter);
        }
    }

    //--****************-- Get the Person List that contains the Search Input --***************--
    private void getPersonsList(Map<String, Persons> allPeople, List<Object> objectList)
    {
        for (Persons person: allPeople.values()) {
            if (person.getPersonFirstName().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(person);
            }
            else if (person.getPersonLastName().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(person);
            }
        }
    }

    //--****************-- Get the Event List that contains the Search Input --***************--
    private void getEventsList(Map<String, Events> availableEvents, List<Object> objectList)
    {
        for (Events event: availableEvents.values()) {
            if (event.getEventType().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(event);
            }
            else if (event.getEventCountry().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(event);
            }
            else if (event.getEventCity().toLowerCase().contains(searchInput.toLowerCase())){
                objectList.add(event);
            }
        }
    }
}
