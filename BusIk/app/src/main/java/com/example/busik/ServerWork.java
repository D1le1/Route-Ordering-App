package com.example.busik;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerWork  {
    private Socket socket;
    private Scanner input;
    private PrintWriter output;
    private String response;

    public void connectToServer(TextView tw)
    {
        new Thread(() -> {
            try {
                socket = new Socket("192.168.100.6", 8001);
                Log.v("ALERTT", "Connected");
                output = new PrintWriter(socket.getOutputStream());
                input = new Scanner(socket.getInputStream());
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    public String sendRequest(String request) throws IOException
    {
        if(output != null) {
            output.println(request);
            output.flush();
        }
        if(input != null && input.hasNextLine())
            return input.nextLine();
        return null;
    }

    public String sendMessageToServer(String message)
    {
        new Thread(() -> {
            if(output != null) {
                output.println(message);
                output.flush();
            }
        }).start();
        return input.nextLine();
    }

    public String getResponse()
    {
        return response;
    }
}

