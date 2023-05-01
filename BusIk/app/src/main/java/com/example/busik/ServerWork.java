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
//                new Thread(() -> {
//                    while (null != (response = input.nextLine()))
//                    {
////                        switch (response)
////                        {
////                            case "AUTH--OK":
////
////                        }
//                    }
//                }).start();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    private void authorization(String login)
    {
        new Thread(() -> {
            if(output != null) {
                output.println("AUTH--" + login);
                output.flush();
            }
//            if(response.equals("AUTH--OK"))

        }).start();

    }

    public String sendRequest(String request) throws IOException
    {

        new Thread(() -> {
            if(output != null) {
                output.println(request);
                output.flush();
            }
        }).start();
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

    public String sendMessage(String mes)
    {
        if(output != null)
        {
            output.println(mes);
            output.flush();
        }
        while (input.hasNextLine())
            return input.nextLine();
        return null;
    }

    public String getResponse()
    {
        return response;
    }
}

