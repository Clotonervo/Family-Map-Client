package com.h0pkins3.familymap.userInterface.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.h0pkins3.familymap.R;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.h0pkins3.familymap.models.Filter;
import com.h0pkins3.familymap.models.MapColor;
import com.h0pkins3.familymap.models.Model;
import com.h0pkins3.familymap.models.Settings;
import com.h0pkins3.familymap.models.baseModels.Events;
import com.h0pkins3.familymap.models.baseModels.Persons;
import com.h0pkins3.familymap.userInterface.activities.FilterActivity;
import com.h0pkins3.familymap.userInterface.activities.PersonActivity;
import com.h0pkins3.familymap.userInterface.activities.SearchActivity;
import com.h0pkins3.familymap.userInterface.activities.SettingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** MyMapFragment
 * Contains all information regarding the map aspects of the application, and is used for the Event activity
 */
public class MyMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Map<Marker, Events> mMarkerMap;
    private Map<String, Events> currentDisplayedEvents;
    private Marker selectedMarker;

    private List<Polyline> lineList;

    private TextView mName;
    private TextView mEvent;
    private TextView mYear;

    private ImageView mIcon;
    private boolean isEvent;

    private Model model = Model.initialize();

    // ========================== Constructors ========================================
    public MyMapFragment()
    {}

    public MyMapFragment (String eventId)
    {
        isEvent = eventId != null;
    }

    ///////////// Text OnClickListener /////////////////////
    View.OnClickListener onClickText = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            textClicked();
        }
    };

//______________________________________ onCreate and other Fragment functions _________________________________________________
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(!isEvent);
    }

    //--****************************-- onCreateView --*******************************--
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View v = layoutInflater.inflate(R.layout.fragment_my_map, viewGroup, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mName = v.findViewById(R.id.person_name);
        mEvent = v.findViewById(R.id.event_details);
        mYear = v.findViewById(R.id.year);
        mIcon = v.findViewById(R.id.map_icon);

        lineList = new ArrayList<>();

        return v;
    }

    //--****************************-- onResume --*******************************--
    @Override
    public void onResume()
    {
        super.onResume();

        if (mMap != null && mMarkerMap != null){
            clearMap();
            Events markedEvent = mMarkerMap.get(selectedMarker);
            putMarkers(mMap);

            if (selectedMarker == null) {
                if (!mMarkerMap.containsValue(markedEvent)) {
                    removeLines();
                }
            }
            mMap.setMapType(model.getSettings().getCurrMapType());
        }

        if (selectedMarker != null && mMarkerMap != null) {
            drawLines();
        }
    }


    //--****************************-- Options Menu Functions --*******************************--
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menus, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.menu_item_filter:
                filterClicked();
                return true;
            case R.id.menu_item_search:
                searchClicked();
                return true;
            case R.id.menu_item_settings:
                settingsClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //--****************************-- Different onClick functions --*******************************--
    private void filterClicked()
    {
        Intent intent = new Intent(getActivity(), FilterActivity.class);
        startActivity(intent);
    }

    private void searchClicked()
    {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    private void settingsClicked()
    {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    private void textClicked()
    {
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        Persons person = model.getPeople().get(mMarkerMap.get(selectedMarker).getEventPersonID());
        model.setSelectedPerson(person);
        startActivity(intent);
    }


    //______________________________________ onMapReady and Other Map Functions _________________________________________________
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        putMarkers(googleMap);
    }

    //--****************-- Puts/Refreshes the Map Markers --*****************--
    private void putMarkers(GoogleMap googleMap)
    {
        selectedMarker = null;
        mMarkerMap = new HashMap<>();

        Map<String, MapColor> allMapColors = model.getEventColor();
        currentDisplayedEvents = model.getDisplayedEvents();

        mMap = googleMap;
        mMap.setMapType(Model.initialize().getSettings().getCurrMapType());

        ////////// Map Marker Click Listener ///////////
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                markerClicked(marker);
                return true;
            }
        });

        for (Events currEvent : currentDisplayedEvents.values()) {
            LatLng currentPosition = new LatLng(currEvent.getEventLatitude(), currEvent.getEventLongitude());
            MapColor mapColor = allMapColors.get(currEvent.getEventType().toLowerCase());

            Marker marker = mMap.addMarker(new MarkerOptions().position(currentPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(mapColor.getColor()))
                    .title(currEvent.getEventType()));
            mMarkerMap.put(marker, currEvent);

            if (model.getSelectedEvent() == currEvent){  // For Event Fragment selection
                selectedMarker = marker;
            }
        }

        if (selectedMarker != null && isEvent){  // Event Fragment camera focus
            mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedMarker.getPosition()));
            markerClicked(selectedMarker);
        }
    }

    //--****************-- Clears All Markers from Map --*****************--
    private void clearMap()
    {
        for (Marker currMarker:mMarkerMap.keySet()) {
            currMarker.remove();
        }
    }

    //--****************************-- markerClicked --*******************************--
    private void markerClicked(Marker marker)
    {
        Events currEvent = mMarkerMap.get(marker);
        Persons currPerson = model.getPeople().get(currEvent.getEventPersonID());
        String newName = currPerson.getPersonFirstName() + " " + currPerson.getPersonLastName();
        String eventInfo = currEvent.getEventType() + ": " + currEvent.getEventCity() + ", " + currEvent.getEventCountry();
        String yearInfo = "(" + currEvent.getEventYear() + ")";

        mName.setText(newName);
        mName.setVisibility(View.VISIBLE);
        mName.setOnClickListener(onClickText);

        mEvent.setText(eventInfo);
        mEvent.setVisibility(View.VISIBLE);
        mEvent.setOnClickListener(onClickText);

        mYear.setText(yearInfo);
        mYear.setVisibility(View.VISIBLE);
        mYear.setOnClickListener(onClickText);

        if (currPerson.getPersonGender().toLowerCase().equals("m")){
            mIcon.setImageDrawable(getResources().getDrawable(R.drawable.icons8_male_52));
        }
        else {
            mIcon.setImageDrawable(getResources().getDrawable(R.drawable.icons8_female_52));
        }
        mIcon.setVisibility(View.VISIBLE);
        mIcon.setOnClickListener(onClickText);

        selectedMarker = marker;
        model.setSelectedEvent(currEvent);
        drawLines();
    }


//______________________________________ Drawing Map Lines Functions _________________________________________________
    private void drawLines()
    {
        Settings settings = Model.initialize().getSettings();

        removeLines();

        if (settings.isStoryLines()){
            drawStoryLines();
        }
        if (settings.isSpouseLines()){
            drawSpouseLines();
        }
        if (settings.isFamilyLines()){
            drawFamilyLines();
        }
    }

    //--****************-- Removes all Lines --*****************--
    private void removeLines()
    {
        for (com.google.android.gms.maps.model.Polyline currLine : lineList) {
            currLine.remove();
        }
        lineList = new ArrayList<Polyline>();
    }

    //--****************-- Start Drawing Story Lines --*****************--
    private void drawStoryLines() {
        Model model = Model.initialize();
        Events currEvent = mMarkerMap.get(selectedMarker);
        Persons currPerson = model.getPeople().get(currEvent.getEventPersonID());
        List<Events> eventsList = model.getAllPersonEvents().get(currPerson.getPersonID());
        eventsList = model.sortEventsByYear(eventsList);

        if (!model.getFilter().containsEventType(currEvent.getEventType())) {
            return;
        }

        firstStoryLine(eventsList);
    }

    //--****************-- Finds first valid event --*****************--
    private void firstStoryLine(List<Events> eventsList)
    {
        int i = 0;
        while (i < eventsList.size() - 1) {
            if (model.getDisplayedEvents().containsValue(eventsList.get(i))) {
                Events event = eventsList.get(i);
                i++;

                secondStoryLine(event, eventsList, i);
            }
            else {
                i++;
            }
        }
    }

    //--****************-- finds Second valid event and draws line --*****************--
    private void secondStoryLine(Events eventOne, List<Events> eventsList, int i)
    {
        while (i < eventsList.size()) {

            if (model.getDisplayedEvents().containsValue(eventsList.get(i))) {
                Events eventTwo = eventsList.get(i);

                Polyline newestLine = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(eventOne.getEventLatitude(), eventOne.getEventLongitude()),
                                new LatLng(eventTwo.getEventLatitude(), eventTwo.getEventLongitude()))
                        .color(model.getSettings().getStoryColor()));
                lineList.add(newestLine);

                return;
            }
            i++;
        }
    }

    //--****************-- Draws Spouse Lines to earliest valid event --*****************--
    private void drawSpouseLines()
    {
        Events currEvent = mMarkerMap.get(selectedMarker);
        Persons currPerson = model.getPeople().get(currEvent.getEventPersonID());
        List<Events> eventsList = model.getAllPersonEvents().get(currPerson.getPersonSpouseID());
        eventsList = model.sortEventsByYear(eventsList);
        Filter filter = model.getFilter();

        if (filter.containsEventType(currEvent.getEventType())) {
            for (int i = 0; i < eventsList.size(); i++) {
                if (model.getDisplayedEvents().containsValue(eventsList.get(i))) {
                    Events spouseValidEvent = eventsList.get(i);

                    Polyline newestLine = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(spouseValidEvent.getEventLatitude(), spouseValidEvent.getEventLongitude()),
                                    new LatLng(currEvent.getEventLatitude(), currEvent.getEventLongitude()))
                            .color(model.getSettings().getSpouseColor()));
                    lineList.add(newestLine);

                    return;
                }
            }
        }
    }

    //--****************-- Starts the family Lines Recursion --*****************--
    private void drawFamilyLines()
    {
        Events currEvent = mMarkerMap.get(selectedMarker);
        Persons currPerson = model.getPeople().get(currEvent.getEventPersonID());

        familyLineHelper(currPerson, currEvent, 10);
    }

    //--****************-- Splits the two paths up the family tree --*****************--
    private void familyLineHelper(Persons currPerson, Events focusedEvent, int generation)
    {
        if (currPerson.getPersonFatherID() != null) {
            familyLineHelperFather(currPerson, focusedEvent, generation);
        }
        if (currPerson.getPersonMotherID() != null){
            familyLineHelperMother(currPerson, focusedEvent, generation);
        }
    }

    //--****************-- Draws Lines to each valid person on the Father's Side --*****************--
    private void familyLineHelperFather(Persons currPerson, Events focusedEvent, int generation)
    {
        List<Events> eventsList = model.getAllPersonEvents().get(currPerson.getPersonFatherID());
        eventsList = model.sortEventsByYear(eventsList);

        for (int i = 0; i < eventsList.size(); i++) {
            if (currentDisplayedEvents.containsValue(eventsList.get(i))) {
                Events validEvent = eventsList.get(i);

                Polyline newestLine = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(focusedEvent.getEventLatitude(), focusedEvent.getEventLongitude()),
                                new LatLng(validEvent.getEventLatitude(), validEvent.getEventLongitude()))
                        .color(model.getSettings().getFamilyColor())
                        .width(generation));
                lineList.add(newestLine);

                Persons father = model.getPeople().get(currPerson.getPersonFatherID());
                familyLineHelper(father, validEvent, generation / 2);
                return;
            }
        }

    }

    //--****************-- Draws Lines to each valid person on the Mother's Side --*****************--
    private void familyLineHelperMother(Persons currPerson, Events focusedEvent, int generation)
    {
        List<Events> eventsList = model.getAllPersonEvents().get(currPerson.getPersonMotherID());
        eventsList = model.sortEventsByYear(eventsList);

        for (int i = 0; i < eventsList.size(); i++) {
            if (currentDisplayedEvents.containsValue(eventsList.get(i))) {
                Events validEvent = eventsList.get(i);

                Polyline newestLine = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(focusedEvent.getEventLatitude(), focusedEvent.getEventLongitude()),
                                new LatLng(validEvent.getEventLatitude(), validEvent.getEventLongitude()))
                        .color(model.getSettings().getFamilyColor())
                        .width(generation));
                lineList.add(newestLine);

                Persons mother = model.getPeople().get(currPerson.getPersonMotherID());
                familyLineHelper(mother, validEvent, generation / 2);
                return;
            }
        }
    }



}


