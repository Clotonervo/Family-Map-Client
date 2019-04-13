package com.h0pkins3.familymap.net.tasks;

import android.os.AsyncTask;

import com.h0pkins3.familymap.models.Model;
import com.h0pkins3.familymap.net.ServerProxy;
import com.h0pkins3.familymap.net.requests.RegisterRequest;
import com.h0pkins3.familymap.net.results.RegisterResult;


/** RegisterTask
 * The RegisterTask extends AsyncTask and is used to check validity of a register Request,
 * and then pulls information from server using a DataTask
 */
public class RegisterTask extends AsyncTask<RegisterRequest, RegisterResult, RegisterResult> implements DataTask.DataContext{

    private String serverHost;
    private String ipAddress;
    private RegisterContext context;

    ////////// Interface ///////////
    public interface RegisterContext {
        void onExecuteComplete(String message);
    }

    // ========================== Constructor ========================================
    public RegisterTask(String server, String ip, RegisterContext c)
    {
        serverHost = server;
        ipAddress = ip;
        context = c;
    }

    //--****************-- Do In Background --***************--
    @Override
    protected RegisterResult doInBackground(RegisterRequest... registerRequests)
    {
        ServerProxy serverProxy = ServerProxy.initialize();
        RegisterResult regResult = serverProxy.register(serverHost, ipAddress, registerRequests[0]);
        return regResult;
    }

    //--****************-- On Post Execute --***************--
    @Override
    protected void onPostExecute(RegisterResult registerResult)
    {
        if (registerResult.getErrorMessage() == null){
            DataTask dataTask = new DataTask(serverHost, ipAddress, this);
            dataTask.execute(registerResult.getToken());
        }
        else {
            context.onExecuteComplete(registerResult.getErrorMessage());
        }
    }


    //--****************-- Completion from DataTask --***************--
    @Override
    public void onExecuteCompleteData(String message)
    {
        context.onExecuteComplete(message);
    }
}
