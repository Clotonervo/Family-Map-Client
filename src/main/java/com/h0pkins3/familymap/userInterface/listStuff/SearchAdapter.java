package com.h0pkins3.familymap.userInterface.listStuff;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h0pkins3.familymap.R;
import com.h0pkins3.familymap.models.Model;
import com.h0pkins3.familymap.models.baseModels.Events;
import com.h0pkins3.familymap.models.baseModels.Persons;
import com.h0pkins3.familymap.userInterface.activities.EventActivity;
import com.h0pkins3.familymap.userInterface.activities.PersonActivity;
import com.h0pkins3.familymap.userInterface.activities.SearchActivity;

import java.util.List;
import java.util.zip.Inflater;

/** SearchAdapter
 * Contains all information about the Search Adapter for the Search Recycler View
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

    private List<Object> mObjects;
    private Context context;
    private LayoutInflater inflater;

    // ========================== Constructor ========================================
    public SearchAdapter(List<Object> objects, Context context)
    {
        this.context = context;
        this.mObjects = objects;
        inflater = LayoutInflater.from(context);
    }

    //--****************-- Creates the View Holder --***************--
    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.list_item_event, parent, false);
        return new SearchHolder(view);
    }

    //--****************-- Binds the View Holder to a SearchHolder --***************--
    @Override
    public void onBindViewHolder(SearchHolder holder, int position)
    {
        final Object currObject = mObjects.get(position);
        if (currObject instanceof Persons){
            holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    personsClicked((Persons) currObject);
                }
            });
            holder.bindPerson(currObject);
        }
        else{
            holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    eventClicked((Events) currObject);
                }
            });
            holder.bindEvent(currObject);
        }
    }

    //--****************-- Gets size of items --***************--
    @Override
    public int getItemCount()
    {
        return mObjects.size();
    }

    //--****************-- Switch to Event Activity --***************--
    private void eventClicked(Events event)
    {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra("Event", "Event");
        Model.initialize().setSelectedEvent(event);
        context.startActivity(intent);
    }

    //--****************-- Switch to Person Activity --***************--
    private void personsClicked(Persons person)
    {
        Intent intent = new Intent(context, PersonActivity.class);
        Model.initialize().setSelectedPerson(person);
        context.startActivity(intent);
    }
}
