package com.h0pkins3.familymap.net;

import com.h0pkins3.familymap.net.requests.*;
import com.h0pkins3.familymap.net.results.*;
import java.net.*;
import java.io.*;
import com.google.gson.*;

/** ServerProxy
 * The ServerProxy is the bridge between the server and the Model class
 */
public class ServerProxy {

    private static ServerProxy serverProxy;

// ========================== Singleton Constructor ========================================
    public static ServerProxy initialize()
    {
        if (serverProxy == null){
            serverProxy = new ServerProxy();
        }
        return serverProxy;
    }


    //____________________________________ Login _________________________________
    public LoginResult login(String serverHost, String serverPort, LoginRequest loginRequest)
    {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            String requestInfo = gson.toJson(loginRequest);
            OutputStream body = http.getOutputStream();
            writeString(requestInfo, body);

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                LoginResult loginResult = gson.fromJson(respData, LoginResult.class);
                return loginResult;
            }
            else {
                return new LoginResult(http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new LoginResult("Error with Login");
        }
    }

    //____________________________________ Register _________________________________
    public RegisterResult register(String serverHost, String serverPort, RegisterRequest regReq)
    {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            String requestInfo = gson.toJson(regReq);
            OutputStream body = http.getOutputStream();
            writeString(requestInfo, body);

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                RegisterResult regResult = gson.fromJson(respData, RegisterResult.class);
                return regResult;
            }
            else {
                return new RegisterResult(http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new RegisterResult("Error with Registering User");
        }
    }

    //____________________________________ Get all People _________________________________
    public AllPersonResults getAllPeople(String serverHost, String serverPort, String authToken)
    {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                AllPersonResults allPersonResults = gson.fromJson(respData, AllPersonResults.class);
                return allPersonResults;
            }
            else {
                return new AllPersonResults(http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new AllPersonResults("Error with retrieving all people");
        }
    }

    //____________________________________ Get all Events _________________________________
    public AllEventResults getAllEvents(String serverHost, String serverPort, String authToken)
    {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                AllEventResults allEventResults = gson.fromJson(respData, AllEventResults.class);
                return allEventResults;
            }
            else {
                return new AllEventResults(http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new AllEventResults("Error with retrieving all events");
        }
    }

    //--****************-- InputStream to String --***************--
    private static String readString(InputStream is) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    //--****************-- Write a String from an OutputStream --***************--
    private static void writeString(String str, OutputStream os) throws IOException
    {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
