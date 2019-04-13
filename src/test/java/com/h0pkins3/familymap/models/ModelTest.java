package com.h0pkins3.familymap.models;

import android.provider.Contacts;
import android.view.Display;

import com.h0pkins3.familymap.models.baseModels.Events;
import com.h0pkins3.familymap.models.baseModels.Persons;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModelTest {


    @Before
    public void setUp()
    {

        //String eventID, String eventDescendantID, String eventPersonID, double eventLatitude,
        //  double eventLongitude, String eventCountry, String eventCity, String eventType, int eventYear

        Events eventOne = new Events("1", "no", "1", 1000, 4000,"m","tokyo", "death", 1960);
        Events eventTwo = new Events("2", "no", "1", 999, 3333,"stuff","yessir", "more death", 1969);
        Events eventThree = new Events("3", "no", "1", 494, 102904,"not America","not New York", "birth", 1870);
        Events eventFour = new Events("4", "no", "1", 4293, 4059309,"Iraq","1234", "death", 1400);

       // String personID, String descendantID, String personFirstName, String personLastName,String personGender,
        // String personFatherID, String personMotherID, String personSpouseID

        Persons personOne = new Persons("1", "no", "false", "john", "f","4","3", "2");
        Persons personTwo = new Persons("2", "no","Jack","Frost","f",null,null,"1");
        Persons personThree = new Persons("3", "no","jenny","F.","m","2",null,"4");
        Persons personFour = new Persons("4", "no","jenny","F.","m",null,null,"3");
        Persons onlyMom = new Persons("mom", "no", "mumma", "gump", "f",null,null,null);


        Persons userOne = new Persons("no", "nope", "false", "john", "doe","1","mom", null);

        Events[] eventsArray = new Events[] {eventOne, eventTwo, eventThree, eventFour};
        Persons[] personArray = new Persons[] {userOne, personOne, personTwo, personThree, personFour, onlyMom};

        Map<String, Persons> personsMap = new HashMap<String, Persons>();
        Model model = Model.initialize();
        model.setUsers(userOne);

        for(int i = 0; i < personArray.length; i++){
            String personID = personArray[i].getPersonID();
            personsMap.put(personID, personArray[i]);
        }

        Map<String, Events> eventsMap = new HashMap<String, Events>();

        for(int i = 0; i < eventsArray.length; i++){
            String eventID = eventsArray[i].getEventID();
            eventsMap.put(eventID, eventsArray[i]);
        }

        model.setEvents(eventsMap);
        model.setPeople(personsMap);
        model.initializeAllData();

    }

    @Test
    public void getPeople()             // all people retrival check
    {
        Model model = Model.initialize();
        Map<String, Persons> test = model.getPeople();
        Assert.assertNotNull(test);
        Assert.assertEquals(6, test.size());

        Persons personOne = new Persons("1", "no", "false", "john", "doe","4","3", "2");
        Assert.assertEquals(personOne.getPersonFatherID(), test.get("1").getPersonFatherID());

        Persons personTwo = new Persons("2", "no","Jack","Frost","f",null,null,null);
        Assert.assertEquals(personTwo.getPersonGender(), test.get("2").getPersonGender());

        Persons personThree = new Persons("3", "no","jenny","F.","m",null,"yup",null);
        Assert.assertEquals(personThree.getPersonLastName(), test.get("3").getPersonLastName());

        Persons personFour = new Persons("4", "no","jenny","F.","m",null,"yup",null);
        Assert.assertEquals(personFour.getPersonFirstName(), test.get("4").getPersonFirstName());

        Persons onlyMom = new Persons("mom", "no", "mumma", "gump", "m",null,null,null);
        Assert.assertEquals(onlyMom.getDescendantID(), test.get("mom").getDescendantID());

        Assert.assertNotEquals(personFour.getPersonFirstName(), test.get("2").getPersonFirstName());
    }

    @Test
    public void getEvents()                 //all events retrieval check
    {
        Model model = Model.initialize();
        Map<String, Events> test = model.getEvents();
        Assert.assertNotNull(test);
        Assert.assertEquals(4, test.size());

        Events eventOne = new Events("1", "no", "1", 1000, 4000,"m","tokyo", "death", 1969);
        Assert.assertEquals(eventOne.getEventCity(), test.get("1").getEventCity());

        Events eventTwo = new Events("2", "no", "1", 999, 3333,"stuff","yessir", "more death", 1900);
        Assert.assertEquals(eventTwo.getEventCountry(), test.get("2").getEventCountry());

        Events eventThree = new Events("3", "no", "1", 494, 102904,"not America","not New York", "birth", 1870);
        Assert.assertEquals(eventThree.getEventID(), test.get("3").getEventID());

        Events eventFour = new Events("4", "no", "1", 4293, 4059309,"Iraq","1234", "death", 1400);
        Assert.assertEquals(eventFour.getEventLatitude(), test.get("4").getEventLatitude(), .05);

        Assert.assertNotEquals(eventFour.getEventID(), test.get("2").getEventID());
    }

    @Test
    public void getPaternalAncestors()              //paternal ancestor check
    {
        Model model = Model.initialize();
        Set<String> paternalAncestors = model.getPaternalAncestors();

        Assert.assertNotNull(paternalAncestors);
        Assert.assertEquals(4, paternalAncestors.size());
        Assert.assertTrue(paternalAncestors.contains("1"));
        Assert.assertTrue(paternalAncestors.contains("2"));
        Assert.assertTrue(paternalAncestors.contains("3"));
        Assert.assertTrue(paternalAncestors.contains("4"));
        Assert.assertFalse(paternalAncestors.contains("mom"));
    }

    @Test
    public void getMaternalAncestors()          //maternal ancestor check
    {
        Model model = Model.initialize();
        Set<String> maternalAncestors = model.getMaternalAncestors();

        Assert.assertNotNull(maternalAncestors);
        Assert.assertEquals(1, maternalAncestors.size());
        Assert.assertTrue(maternalAncestors.contains("mom"));
        Assert.assertFalse(maternalAncestors.contains("4"));
    }

    @Test
    public void sortEventsByYear()              //sort by year
    {
        Model model = Model.initialize();
        List<Events> eventsArrayList = model.getAllPersonEvents().get("1");
        Assert.assertNotNull(eventsArrayList);

        Events eventOne = new Events("1", "no", "1", 1000, 4000,"m","tokyo", "death", 1960);
        Events eventTwo = new Events("2", "no", "1", 999, 3333,"stuff","yessir", "more death", 1969);
        Events eventThree = new Events("3", "no", "1", 494, 102904,"not America","not New York", "birth", 1870);
        Events eventFour = new Events("4", "no", "1", 4293, 4059309,"Iraq","1234", "death", 1400);

        Assert.assertEquals(eventOne, eventsArrayList.get(0));
        Assert.assertEquals(eventFour, eventsArrayList.get(3));

        eventsArrayList = model.sortEventsByYear(eventsArrayList);

        Assert.assertEquals(eventFour, eventsArrayList.get(0));
        Assert.assertEquals(eventThree, eventsArrayList.get(1));
        Assert.assertEquals(eventOne, eventsArrayList.get(2));
        Assert.assertEquals(eventTwo, eventsArrayList.get(3));
    }

    @Test
    public void filterEvents()              //filter by event type
    {
        Model model = Model.initialize();
        Filter filter = model.getFilter();

        Map<String, Events> test = model.getDisplayedEvents();
        Assert.assertNotNull(test);
        Assert.assertEquals(4, test.size());

        Events eventOne = new Events("1", "no", "1", 1000, 4000,"m","tokyo", "death", 1969);
        Assert.assertEquals(eventOne.getEventCity(), test.get("1").getEventCity());

        Events eventTwo = new Events("2", "no", "1", 999, 3333,"stuff","yessir", "more death", 1900);
        Assert.assertEquals(eventTwo.getEventCountry(), test.get("2").getEventCountry());

        Events eventThree = new Events("3", "no", "1", 494, 102904,"not America","not New York", "birth", 1870);
        Assert.assertEquals(eventThree.getEventID(), test.get("3").getEventID());

        Events eventFour = new Events("4", "no", "1", 4293, 4059309,"Iraq","1234", "death", 1400);
        Assert.assertEquals(eventFour.getEventLatitude(), test.get("4").getEventLatitude(), .05);

        Assert.assertNotEquals(eventFour.getEventID(), test.get("2").getEventID());

        filter.getDisplayedEvents().remove("death");

        test = model.getDisplayedEvents();
        Assert.assertNotNull(test);
        Assert.assertEquals(2, test.size());

        Assert.assertEquals(eventTwo.getEventCity(), test.get("2").getEventCity());
        Assert.assertEquals(eventThree.getEventCountry(), test.get("3").getEventCountry());
        Assert.assertFalse(test.containsKey("1"));
        Assert.assertFalse(test.containsKey("4"));

        filter.getDisplayedEvents().add("death");
    }

    @Test
    public void filterPeople()              //filter by people
    {
        Model model = Model.initialize();
        Filter filter = model.getFilter();

        Map<String, Events> test = model.getDisplayedEvents();
        Assert.assertNotNull(test);
        Assert.assertEquals(4, test.size());

        Events eventOne = new Events("1", "no", "1", 1000, 4000,"m","tokyo", "death", 1969);
        Assert.assertEquals(eventOne.getEventCity(), test.get("1").getEventCity());

        Events eventTwo = new Events("2", "no", "1", 999, 3333,"stuff","yessir", "more death", 1900);
        Assert.assertEquals(eventTwo.getEventCountry(), test.get("2").getEventCountry());

        Events eventThree = new Events("3", "no", "1", 494, 102904,"not America","not New York", "birth", 1870);
        Assert.assertEquals(eventThree.getEventID(), test.get("3").getEventID());

        Events eventFour = new Events("4", "no", "1", 4293, 4059309,"Iraq","1234", "death", 1400);
        Assert.assertEquals(eventFour.getEventLatitude(), test.get("4").getEventLatitude(), .05);
        Assert.assertNotEquals(eventFour.getEventID(), test.get("2").getEventID());

        filter.setFemales(false);

        test = model.getDisplayedEvents();
        Assert.assertNotNull(test);
        Assert.assertEquals(0, test.size());
    }

}