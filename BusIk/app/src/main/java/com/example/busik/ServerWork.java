package com.example.busik;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerWork  {
    private static Socket socket;
    private static Scanner input;
    private static PrintWriter output;
    private static boolean connected = false;
    private static boolean requestInProgress = false;
    private static Object mutex = new Object();
    private static Thread connectThread;

    public static void connectToServer() {
        connectThread = new Thread(() -> {
            try {
                socket = new Socket("192.168.100.6", 8001);
                Log.v("ALERTT", "Connected");
                connected = true;
                output = new PrintWriter(socket.getOutputStream());
                input = new Scanner(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            while (!connected) {
//
//                try {
//                    Thread.sleep(5000); // ожидание 5 секунд перед следующей попыткой подключения
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        });
        connectThread.start();
    }

//    public static String sendRequest(String request) throws IOException
//    {
//        if(connected) {
//            if (output != null) {
//                output.println(request);
//                output.flush();
//            }
//            if (input != null && input.hasNextLine())
//                return input.nextLine();
//            connected = false;
//
//        }else{
//            connectToServer();
//            if(connected)
//                return sendRequest(request);
//            else
//                return null;
//        }
////        connectToServer();
////        if(connected)
////            return sendRequest(request);
//        return null;
//    }

    public static String sendRequest(String request) throws IOException {
        if(!connected)
        {
            connectToServer();
            try{
                Thread.sleep(400);
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        if(connected && !requestInProgress) {
            requestInProgress = true;
            if (output != null) {
                output.println(request);
                output.flush();
            }
            if (input != null && input.hasNextLine()) {
                requestInProgress = false;
                return input.nextLine();
            }
            requestInProgress = false;
            connected = false;
        }
        return null;
    }

    public static boolean isClientConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
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
}

