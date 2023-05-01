package com.example.busik;

import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Spliterator;

public class ServerWork {
    private static Socket clientSocket;
    private static Scanner input;
    private static PrintWriter output;
    private static String username;
    private static boolean isConnected = false;

    public static void connectToServer()
    {
        int port = 8001;

        System.out.println("Connecting...");
        Log.v("ALERTT", "Connecting...");
        try{
            clientSocket = new Socket("localhost", port);
            Log.v("ALERTT", "Connected");
            isConnected = true;
//            Thread inputThread = new Thread(() -> {
//                try{
//                    while (isConnected)
//                    {
//                        if(clientSocket.isClosed())
//                        {
//                            break;
//                        }
//                        if(input.hasNext())
//                        {
//                            String inMsg = input.nextLine();
//                            System.out.println(inMsg);
//                        }
//                    }
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            });
//            inputThread.start();
//            while(true) {
//                String msg = scn.nextLine();
//                if(msg.equalsIgnoreCase("quit"))
//                {
//                    output.println("##shutdown##");
//                    output.flush();
//                    System.out.println("You're disconnected");
//                    isConnected = false;
//                    break;
//                }
//                else {
//                    output.println(msg);
//                    output.flush();
//                }
//            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {

    }
}

