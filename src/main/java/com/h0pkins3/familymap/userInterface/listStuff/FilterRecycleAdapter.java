package com.h0pkins3.familymap.userInterface.listStuff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.h0pkins3.familymap.R;
import com.h0pkins3.familymap.models.Filter;
import com.h0pkins3.familymap.models.Model;

import java.util.List;

/** FilterRecyclerAdapter
 * Contains the Adapter information for the Filter Activity Recycler View
 */
public class FilterRecycleAdapter extends RecyclerView.Adapter<FilterHolder> {

    private List<String> eventTypesList;
    private LayoutInflater inflater;

    private Filter filter = Model.initialize().getFilter();

    // ========================== Constructor ========================================
    public FilterRecycleAdapter(List<String> newEventTypes, Context context)
    {
        eventTypesList = newEventTypes;
        inflater = LayoutInflater.from(context);
    }

    //--****************-- Creates the View Holder --***************--
    @Override
    public FilterHolder onCreateViewHolder (ViewGroup viewGroup, final int i)
    {
        View filterView = inflater.inflate(R.layout.list_item_filter, viewGroup, false);
        return new FilterHolder (filterView);
    }

    //--****************-- Binds the View Holder to a FilterHolder --***************--
    @Override
    public void onBindViewHolder(final FilterHolder filterHolder, final int i)
    {
        final String currEventType = eventTypesList.get(i);

        if (i <= 3){
            filterHolder.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    defaultFilterClicked(i, isChecked);
                }
            });
            filterHolder.bindDefaults(currEventType, i);
        }
        else {
            filterHolder.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    eventFilterClicked(i, isChecked);
                }
            });
            filterHolder.bind(currEventType);
        }
    }

    //--****************-- Gets size of items --***************--
    @Override
    public int getItemCount()
    {
        return eventTypesList.size();
    }

    //--****************-- Default Filters onClick Function --***************--
    private void defaultFilterClicked(int index, boolean isChecked)
    {
        switch (index){
            case 0:
                filter.setFathersSide(isChecked);
                break;
            case 1:
                filter.setMothersSide(isChecked);
                break;
            case 2:
                filter.setMales(isChecked);
                break;
            case 3:
                filter.setFemales(isChecked);
                break;
        }
    }

    //--****************-- Event Type Filters onClick Function --***************--
    private void eventFilterClicked(int index, boolean isChecked)
    {
        if (filter.containsEventType(eventTypesList.get(index)) && !isChecked){
            filter.deleteEventType(eventTypesList.get(index));
        }
        else if (!filter.containsEventType((eventTypesList.get(index)))){
            filter.addEvent(eventTypesList.get(index));
        }
    }
}

