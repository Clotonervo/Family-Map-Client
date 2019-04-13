package com.h0pkins3.familymap.models;

import com.h0pkins3.familymap.models.baseModels.*;

import java.util.*;

/** Model
 * The Model class contained all information pulled from the database, and is used to manipulate
 * such data to allow consistency in the application
 */
public class Model {

    private Map<String, Persons> people;
    private Map<String, Events> events;

    private Map<String, Events> displayedEvents;
    private Map<String, List<Events>> allPersonEvents;

    private Settings settings;
    private Filter filter;

    private List<String> eventTypes;
    private Map <String, MapColor> eventColor;
    private Persons user;

    private Set<String> paternalAncestors;
    private Set<String> maternalAncestors;
    private Map<String, Persons> children;

    private String serverHost;
    private String ipAddress;
    private String authToken;

    private Persons selectedPerson;
    private Events selectedEvent;

    private static Model instance;

    // ========================== Singleton Constructor ========================================
    public static Model initialize()
    {
        if (instance == null){
            instance = new Model();
        }
        return instance;
    }

    //_______________________________ Getters and Setters __________________________________________

    public Map<String, Persons> getPeople()
    {
        return people;
    }

    public void setPeople(Map<String, Persons> people)
    {
        this.people = people;
    }

    public Map<String, Events> getEvents()
    {
        return events;
    }

    public void setEvents(Map<String, Events> events)
    {
        this.events = events;
    }

    public void setAllPersonEvents(Map<String, List<Events>> newPersonsWithEvents)
    {
        allPersonEvents = newPersonsWithEvents;
    }

    public Map<String, List<Events>> getAllPersonEvents()
    {
        return allPersonEvents;
    }

    public Settings getSettings()
    {
        return settings;
    }

    public void setSettings(Settings newSettings)
    {
        settings = newSettings;
    }

    public Filter getFilter()
    {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<String> getEventTypes()
    {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes)
    {
        this.eventTypes = eventTypes;
    }

    public Map<String, MapColor> getEventColor()
    {
        return eventColor;
    }

    public void setEventColor(Map<String, MapColor> eventColor)
    {
        this.eventColor = eventColor;
    }

    public Persons getUsers()
    {
        return user;
    }

    public void setUsers(Persons user)
    {
        this.user = user;
    }

    public Set<String> getPaternalAncestors()
    {
        return paternalAncestors;
    }

    public void setPaternalAncestors(Set<String> paternalAncestors)
    {
        this.paternalAncestors = paternalAncestors;
    }

    public Set<String> getMaternalAncestors()
    {
        return maternalAncestors;
    }

    public void setMaternalAncestors(Set<String> maternalAncestors)
    {
        this.maternalAncestors = maternalAncestors;
    }

    public Map<String, Persons> getChildren()
    {
        return children;
    }

    public void setChildren(Map<String, Persons> children)
    {
        this.children = children;
    }

    public String getServerHost()
    {
        return serverHost;
    }

    public void setServerHost(String newServer)
    {
        serverHost = newServer;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(String newIP)
    {
        ipAddress = newIP;
    }

    public void setAuthToken(String newAuth)
    {
        authToken = newAuth;
    }

    public String getAuthToken()
    {
        return authToken;
    }

    public Persons getSelectedPerson()
    {
        return selectedPerson;
    }

    public void setSelectedPerson(Persons selectedPerson)
    {
        this.selectedPerson = selectedPerson;
    }

    public Events getSelectedEvent()
    {
        return selectedEvent;
    }

    public void setSelectedEvent(Events selectedEvent)
    {
        this.selectedEvent = selectedEvent;
    }

    //____________________________________ Data Manipulation Methods _________________________________
    //--****************-- Is Person Included in the Filter --***************--
    public boolean isPersonDisplayed(Persons currPerson)
    {
        if (!filter.isMales() && currPerson.getPersonGender().toLowerCase().equals("m")){
            return false;
        }
        else if (!filter.isFemales() && currPerson.getPersonGender().toLowerCase().equals("f")){
            return false;
        }
        else if (!filter.isFathersSide() && paternalAncestors.contains(currPerson.getPersonID())) {
            return false;
        }
        else return filter.isMothersSide() || !maternalAncestors.contains(currPerson.getPersonID());
    }

    //--****************-- Sort Events By Year --***************--
    public List<Events> sortEventsByYear(List<Events> eventsArrayList)
    {
        List<Events> sortedEventsList = new ArrayList<>();
        List<Events> currArrayList = new ArrayList<>(eventsArrayList);

        while(currArrayList.size() > 0){
            Events currEvent = currArrayList.get(0);
            int index = 0;
            for (int i = 0; i < currArrayList.size(); i++){
                if (currArrayList.get(i).getEventYear() < currEvent.getEventYear()){
                    currEvent = currArrayList.get(i);
                    index = i;
                }
            }
            sortedEventsList.add(currEvent);
            currArrayList.remove(index);
        }
        return sortedEventsList;
    }

    //--****************-- Find all Relatives of a Person --***************--
    public List<Persons> findRelatives(String personID)
    {
        Persons currPerson = getPeople().get(personID);
        List<Persons> personList = new ArrayList<>();

        if(getPeople().get(currPerson.getPersonSpouseID()) != null){
            personList.add(getPeople().get(currPerson.getPersonSpouseID()));
        }
        if(getPeople().get(currPerson.getPersonMotherID()) != null){
            personList.add(getPeople().get(currPerson.getPersonMotherID()));
        }
        if(getPeople().get(currPerson.getPersonFatherID()) != null){
            personList.add(getPeople().get(currPerson.getPersonFatherID()));
        }
        if(getChildren().get(currPerson.getPersonID()) != null){
            personList.add(getChildren().get(currPerson.getPersonID()));
        }

        return personList;
    }

    //--****************-- Get all Events that are Displayed --***************--
    public Map<String, Events> getDisplayedEvents()
    {
        displayedEvents = new HashMap<>();

        for (Events currEvent: events.values()) {
            Persons eventPerson = getPeople().get(currEvent.getEventPersonID());
            if (!isPersonDisplayed(eventPerson)){
            }
            else if (!filter.containsEventType(currEvent.getEventType())){
            }
            else {
                displayedEvents.put(currEvent.getEventID(), currEvent);
            }
        }
        return displayedEvents;
    }

    //____________________________________ Initialize rest of Data _________________________________
    public void initializeAllData()
    {
        initializeEventTypes();
        initializePaternalTree();
        initializeMaternalTree();
        initializeAllPersonEvents();
        initializeAllChildren();
        if (settings == null){
            settings = new Settings();
        }
        if (filter == null) {
            filter = new Filter();
        }
    }

    //--****************-- Event Types --***************--
    private void initializeEventTypes()
    {
        ArrayList<Events> eventsArray = new ArrayList<>();
        for (Events currEvent : events.values()) {
            eventsArray.add(currEvent);
        }

        eventColor = new HashMap<>();
        eventTypes = new ArrayList<>();
        for (int i = 0; i < eventsArray.size(); i++){
            if (!eventColor.containsKey(eventsArray.get(i).getEventType().toLowerCase())){
                eventColor.put(eventsArray.get(i).getEventType().toLowerCase(),
                        new MapColor(eventsArray.get(i).getEventType().toLowerCase()));

                eventTypes.add(eventsArray.get(i).getEventType().toLowerCase());
            }
        }
        instance.setEventTypes(eventTypes);
    }

    //--****************-- Paternal and Maternal Tree Start --***************--
    private void initializePaternalTree()
    {
        paternalAncestors = new HashSet<>();
        ancestorHelper(user.getPersonFatherID(), paternalAncestors);
    }

    private void initializeMaternalTree()
    {
        maternalAncestors = new HashSet<>();
        ancestorHelper(user.getPersonMotherID(), maternalAncestors);
    }

    //--****************-- Ancestor Recursive Helper --***************--
    private void ancestorHelper(String currPersonID, Set<String> personSet)
    {
        if (currPersonID == null){
            return;
        }
        personSet.add(currPersonID);
        Persons currPerson = people.get(currPersonID);

        if (currPerson.getPersonFatherID() != null) {
            ancestorHelper(currPerson.getPersonFatherID(), personSet);
        }

        if (currPerson.getPersonMotherID() != null) {
            ancestorHelper(currPerson.getPersonMotherID(), personSet);
        }
    }

    //--****************-- All Events per Person --***************--
    private void initializeAllPersonEvents()
    {
        allPersonEvents = new HashMap<>();
        for (Persons person: people.values()) {
            ArrayList<Events> eventList = new ArrayList<Events>();

            for (Events event: events.values()) {
                if (person.getPersonID().equals(event.getEventPersonID())){
                    eventList.add(event);
                }
            }
            allPersonEvents.put(person.getPersonID(),eventList);
        }
    }

    //--****************-- All Children of each Person --***************--
    private void initializeAllChildren()
    {
        children = new HashMap<>();
        for (Persons person: people.values()) {

            if (person.getPersonFatherID() != null){
                children.put(person.getPersonFatherID(), person);
            }
            if (person.getPersonMotherID() != null){
                children.put(person.getPersonMotherID(), person);
            }
        }
    }
}
