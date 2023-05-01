package com.D1le;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerChat {
    private static final int PORT = 8001;
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public ServerChat() {

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try{
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started\n");

            while (true)
            {
                ClientHandler client = null;
                try {
                    clientSocket = serverSocket.accept();
                    client = new ClientHandler(clientSocket, this);
                    clients.add(client);
                    new Thread(client).start();
                }catch (Exception e){
                    System.out.println("Error!!");
                }
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            try{
                clients.stream().forEach(x -> x.close());
                System.out.println("Server stopped");
                serverSocket.close();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToAllClients(String msg, ClientHandler client)
    {
        for (ClientHandler c : clients)
        {
            if(!c.equals(client))
            {
                c.sendMsg(client.getUsername() + ": " + msg);
            }
        }
    }

    public void sendNotificationToAllClients(String not, ClientHandler client)
    {
        for(ClientHandler c : clients)
        {
            c.sendMsg(not);
//            if(!c.equals(client))
//                c.sendMsg(not);
        }
    }

    public void showMessageOnServer(String msg){
        System.out.println(msg);
    }

    public void removeClient(ClientHandler client)
    {
        clients.remove(client);
    }
}
