package com.example.busik;

import android.app.Application;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MyApplication extends AsyncTask<Void, Void, String> {
    private Socket socket;

    public void connectToServer()
    {
        try{
            socket = new Socket("192.168.100.6", 8001);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void disconnectFromServer()
    {
        try{
            socket.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return null;
    }
}
