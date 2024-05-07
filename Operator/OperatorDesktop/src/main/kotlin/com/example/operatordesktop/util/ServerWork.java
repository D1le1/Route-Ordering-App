package com.example.operatordesktop.util;

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

    public static void connectToServer() {
        Thread connectThread = new Thread(() -> {
            try {
                socket = new Socket("localhost", 8001);
                connected = true;
                output = new PrintWriter(socket.getOutputStream());
                input = new Scanner(socket.getInputStream());
            } catch (IOException ignored) {
            }
        });
        connectThread.start();
    }

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

