package com.h0pkins3.familymap.net;

import com.h0pkins3.familymap.net.requests.LoginRequest;
import com.h0pkins3.familymap.net.requests.RegisterRequest;
import com.h0pkins3.familymap.net.results.AllEventResults;
import com.h0pkins3.familymap.net.results.AllPersonResults;
import com.h0pkins3.familymap.net.results.LoginResult;
import com.h0pkins3.familymap.net.results.RegisterResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServerProxyTest {

    @Before
    public void setUp()
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        RegisterRequest regReq = new RegisterRequest("test","nope","no","sam","hopkins", "m");
        RegisterResult registerResult = serverProxy.register("127.0.0.1", "8080", regReq);
    }

    @Test
    public void loginSucceed()
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        LoginRequest loginRequest = new LoginRequest("test", "nope");
        LoginResult loginResult = serverProxy.login("127.0.0.1", "8080", loginRequest);
        Assert.assertNotNull(loginResult.getUserName());
    }

    @Test
    public void loginFailUserNotFound()
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        LoginRequest loginRequest = new LoginRequest("yes", "nope");
        LoginResult loginResult = serverProxy.login("127.0.0.1", "8080", loginRequest);
        Assert.assertNotNull(loginResult.getErrorMessage());
    }

    @Test
    public void registerSucceed()
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        RegisterRequest regReq = new RegisterRequest("kk","nope","no","sam","hopkins", "m");
        RegisterResult registerResult = serverProxy.register("127.0.0.1", "8080", regReq);
        Assert.assertNotNull(registerResult.getPersonID());
    }

    @Test
    public void registerFailUserAlreadyExists()
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        RegisterRequest regReq = new RegisterRequest("test","nope","no","sam","hopkins", "m");
        RegisterResult registerResult = serverProxy.register("127.0.0.1", "8080", regReq);
        Assert.assertNotNull(registerResult.getErrorMessage());
    }

    @Test
    public void getAllPeopleSuccess()
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        LoginRequest loginRequest = new LoginRequest("test", "nope");
        LoginResult loginResult = serverProxy.login("127.0.0.1", "8080", loginRequest);
        String authTokenTest = loginResult.getToken();
        AllPersonResults allPersonResults = serverProxy.getAllPeople("127.0.0.1", "8080", authTokenTest);
        Assert.assertNotNull(allPersonResults.getPersonsArray());
    }

    @Test
    public void getAllPeopleFailAuthTokenInvalid()
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        String authTokenTest = "This Token Will Not Work";
        AllPersonResults allPersonResults = serverProxy.getAllPeople("127.0.0.1", "8080", authTokenTest);
        Assert.assertNotNull(allPersonResults.getErrorMessage());
    }

    @Test
    public void getAllEventsSuccess()
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        LoginRequest loginRequest = new LoginRequest("test", "nope");
        LoginResult loginResult = serverProxy.login("127.0.0.1", "8080", loginRequest);
        String authTokenTest = loginResult.getToken();
        AllEventResults allEventsResults = serverProxy.getAllEvents("127.0.0.1", "8080", authTokenTest);
        Assert.assertNotNull(allEventsResults.getEventsArray());
    }

    @Test
    public void getAllEventsFailUserInvalidAuthtoken()
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        String authTokenTest = "this Authtoken is invalid";
        AllEventResults allEventsResults = serverProxy.getAllEvents("127.0.0.1", "8080", authTokenTest);
        Assert.assertNotNull(allEventsResults.getErrorMessage());
    }

}
