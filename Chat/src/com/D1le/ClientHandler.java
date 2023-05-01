package com.D1le;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private Socket clientSocket = null;
    private ServerChat server;
    private PrintWriter output;
    private Scanner input;
    private String username;

    public ClientHandler(Socket clientSocket, ServerChat server) {
        try {
            this.clientSocket = clientSocket;
            this.server = server;
            this.output = new PrintWriter(clientSocket.getOutputStream());
            this.input = new Scanner(clientSocket.getInputStream());

        }catch (IOException e)
        {
            System.out.println("Error1");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            System.out.println("New User connected");
//            String message = input.nextLine();
//            System.out.println("Receive: " + message);
//            output.println("Hello user");
//            output.flush();
//            while (true)
//            {
//                username = input.nextLine();
//                server.sendNotificationToAllClients("User: " + username + " is connected", this);
//                break;
//            }
            String message;
            while ((message = input.nextLine()) != null)
            {
//                System.out.println(username +": " + message);
                System.out.println("Receive: " + message);
//                server.sendMessageToAllClients(message, this);
                Thread.sleep(10);
            }

        } catch (Exception e)
        {
            System.out.println("Error3");
            e.printStackTrace();
        }
        finally {
            this.close();
        }
    }

    public void sendMsg(String msg)
    {
        try{
            if(clientSocket.isConnected()) {
                output.println(msg);
                output.flush();
            }else
            {
                this.close();
            }
        }catch (Exception e)
        {
            System.out.println("Error4");
            e.printStackTrace();
        }
    }

    public String getUsername()
    {
        return username;
    }

    public void close()
    {
        try{
            clientSocket.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        server.removeClient(this);
        server.sendNotificationToAllClients(username + " leave the server", this);
        System.out.println("Closed");
    }
}
