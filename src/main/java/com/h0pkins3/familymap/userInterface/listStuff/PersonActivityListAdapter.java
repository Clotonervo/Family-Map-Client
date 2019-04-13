package com.h0pkins3.familymap.userInterface.listStuff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.h0pkins3.familymap.R;
import com.h0pkins3.familymap.models.Model;
import com.h0pkins3.familymap.models.baseModels.Events;
import com.h0pkins3.familymap.models.baseModels.Persons;

import java.util.List;

/** PersonActivityListAdapter
 * This class contains all information used by the expandable list in the Person Activity
 */
public class PersonActivityListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> mHeaders;
    private List<Events> mEvents;
    private List<Persons> mPersons;
    private Persons mCurrPerson;

    private TextView mFirstLine;
    private TextView mSecondLine;
    private ImageView mListIcon;

    private Model model = Model.initialize();

    // ========================== Constructor ========================================
    public PersonActivityListAdapter(Context context, List<String> listDataHeader,
                                     List<Events> eventsList, List<Persons> personsList,
                                     Persons person) {
        this.context = context;
        this.mHeaders = listDataHeader;
        this.mEvents = eventsList;
        this.mPersons = personsList;
        this.mCurrPerson = person;
    }

    //_______________________________ List Adapter Override Functions __________________________________________
    @Override
    public int getGroupCount()
    {
        return mHeaders.size();
    }

    //--****************-- Get Number of Children --***************--
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (groupPosition == 0){
            return mEvents.size();
        }
        else{
            return mPersons.size();
        }
    }

    //--****************-- Get Drop Down Group --***************--
    @Override
    public Object getGroup(int groupPosition)
    {
        if (groupPosition == 0){
            return mEvents;
        }
        else{
            return mPersons;
        }
    }

    //--****************-- Get Child --***************--
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if(groupPosition == 0){
            return mEvents.get(childPosition);
        }
        else{
            return mPersons.get(childPosition);
        }
    }

    //--****************-- Functions not used, but needed to be Overridden --***************--
    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    //--****************-- Get the Header layout and inflate --***************--
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = mHeaders.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_header_event, null);
        }

        TextView header = convertView.findViewById(R.id.event_header);
        header.setText(headerTitle);

        return convertView;
    }

    //--****************-- Get the Child Layout and inflate --***************--
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_event, null);
        }

        mFirstLine = convertView.findViewById(R.id.event_list_info);
        mSecondLine = convertView.findViewById(R.id.event_list_person);
        mListIcon = convertView.findViewById(R.id.list_item_icon);

        if (groupPosition == 0) {
            Events currEvent = (Events) getChild(groupPosition, childPosition);

            mListIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.map_pointer_icon));
            update(currEvent, null);

        }
        else{
            Persons currPerson = (Persons) getChild(groupPosition, childPosition);

            if (currPerson.getPersonGender().toLowerCase().equals("m")){
                mListIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.icons8_male_52));
            }
            else {
                mListIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.icons8_female_52));
            }

            update(null, currPerson);
        }
        return convertView;
    }

    //--****************-- Set Child to Selectable --***************--
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }


    //--****************-- Initiate Text and Icon of a Child Layout --***************--
    private void update(Events events, Persons persons)
    {
        if (persons == null) {
            String eventInfo = events.getEventType() + ", " + events.getEventCity() + ", " + events.getEventCountry() + " " + events.getEventYear();
            mFirstLine.setText(eventInfo);
            Persons currPerson = model.getPeople().get(events.getEventPersonID());
            String personInfo = currPerson.getPersonFirstName() + " " + currPerson.getPersonLastName();
            mSecondLine.setText(personInfo);
        }
        else {
            String personInfo = persons.getPersonFirstName() + " " + persons.getPersonLastName();
            mFirstLine.setText(personInfo);
            mSecondLine.setText(getRelationship(persons));

        }
    }

    //--****************-- Find Relationships of a Person --***************--
    private String getRelationship(Persons persons)
    {
        if (mCurrPerson.getPersonSpouseID().equals(persons.getPersonID())) {
            return "Spouse";
        }

        if (persons.getPersonFatherID() != null && persons.getPersonMotherID() != null) {
            if (persons.getPersonFatherID().equals(mCurrPerson.getPersonID()) ||
                    persons.getPersonMotherID().equals(mCurrPerson.getPersonID())) {
                return "Child";
            }
        }

        if (mCurrPerson.getPersonMotherID() != null && mCurrPerson.getPersonMotherID() != null) {
            if (mCurrPerson.getPersonFatherID().equals(persons.getPersonID())) {
                return "Father";
            }
            else if (mCurrPerson.getPersonMotherID().equals(persons.getPersonID())) {
                return "Mother";
            }
        }
        return "Error";
    }

}
