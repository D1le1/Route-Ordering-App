package com.D1le;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Spliterator;

public class Client {
    private static Socket clientSocket;
    private static Scanner input;
    private static PrintWriter output;
    private static String username;
    private static Scanner scn = new Scanner(System.in);
    private static boolean isConnected = false;

    public static void main(String[] args)
    {
        int port = args.length == 0 ? 8001 : Integer.parseInt(args[0]);

        System.out.print("Enter username: ");
        username = scn.nextLine();

        System.out.println("Connecting...");
        try{
            clientSocket = new Socket("192.168.24.99", port);
            input = new Scanner(clientSocket.getInputStream());
            output = new PrintWriter(clientSocket.getOutputStream());
            System.out.println("Connection successful");
            isConnected = true;
            output.println(username);
            output.flush();
            Thread inputThread = new Thread(() -> {
                try{
                    while (isConnected)
                    {
                        if(clientSocket.isClosed())
                        {
                            break;
                        }
                        if(input.hasNext())
                        {
                            String inMsg = input.nextLine();
                            System.out.println(inMsg);
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
            inputThread.start();
            while(true) {
                String msg = scn.nextLine();
                if(msg.equalsIgnoreCase("quit"))
                {
                    output.println("##shutdown##");
                    output.flush();
                    System.out.println("You're disconnected");
                    isConnected = false;
                    break;
                }
                else {
                    output.println(msg);
                    output.flush();
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
