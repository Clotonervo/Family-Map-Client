package com.h0pkins3.familymap.net.tasks;

import android.os.AsyncTask;

import com.h0pkins3.familymap.models.Model;
import com.h0pkins3.familymap.models.baseModels.Events;
import com.h0pkins3.familymap.models.baseModels.Persons;
import com.h0pkins3.familymap.net.ServerProxy;
import com.h0pkins3.familymap.net.results.AllEventResults;
import com.h0pkins3.familymap.net.results.AllPersonResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** DataTask
 * DataTask extends the AsyncTask and reaches the server to extract all information regarding user
 * after successful login or register
 */
public class DataTask extends AsyncTask<String, Boolean, Boolean> {

    private String serverHost;
    private String ipAddress;
    private DataContext context;
    private Model model = Model.initialize();

    ///////// Interface //////////
    public interface DataContext {
        void onExecuteCompleteData(String message);
    }

    // ========================== Constructor ========================================
    public DataTask(String server, String ip, DataTask.DataContext c)
    {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    //--****************-- Do In Background --***************--
    @Override
    protected Boolean doInBackground(String... authToken)
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        AllPersonResults allPersonResults = serverProxy.getAllPeople(serverHost, ipAddress, authToken[0]);
        AllEventResults allEventResults = serverProxy.getAllEvents(serverHost, ipAddress, authToken[0]);

        Boolean bool = sendDataToModel(allPersonResults, allEventResults);
        return bool;
    }

    //--****************-- On Post Execute --***************--
    @Override
    protected void onPostExecute(Boolean bool) {
        if (bool){
            Persons user = model.getUsers();
            String message = "Welcome, " + user.getPersonFirstName() + " " + user.getPersonLastName();
            context.onExecuteCompleteData(message);
            model.initializeAllData();
        }
        else {
            context.onExecuteCompleteData("Error occurred with user data");
        }
    }

    //_______________________________ Data Initialization __________________________________________
    private boolean sendDataToModel(AllPersonResults allPersonResults, AllEventResults allEventResults)
    {
        return (initializeAllEvents(allEventResults) && initializeAllPeople(allPersonResults));
    }

    //--****************-- Initializing People --***************--
    private boolean initializeAllPeople(AllPersonResults allPersonResults)
    {
        if (allPersonResults.getErrorMessage() == null){
            Map<String, Persons> personsMap = new HashMap<String, Persons>();
            ArrayList<Persons> personArray = allPersonResults.getPersonsArray();
            model.setUsers(personArray.get(0));

            for(int i = 0; i < personArray.size(); i++){
                String personID = personArray.get(i).getPersonID();
                personsMap.put(personID, personArray.get(i));
            }

            model.setPeople(personsMap);
            return true;
        }
        return false;
    }

    //--****************-- Initializing Events --***************--
    private boolean initializeAllEvents(AllEventResults allEventResults)
    {
        if (allEventResults.getErrorMessage() == null){
            Map<String, Events> eventsMap = new HashMap<String, Events>();
            ArrayList<Events> eventsArray = allEventResults.getEventsArray();

            for(int i = 0; i < eventsArray.size(); i++){
                String eventID = eventsArray.get(i).getEventID();
                eventsMap.put(eventID, eventsArray.get(i));
            }

            model.setEvents(eventsMap);
            return true;
        }
        return false;
    }

}
