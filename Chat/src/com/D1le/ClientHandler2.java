package com.D1le;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler2 implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private Scanner in;

    public ClientHandler2(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new Scanner(clientSocket.getInputStream());

            String inputLine;
            while (null != (inputLine = in.nextLine())) {
                System.out.println("Receive: " + inputLine);
                String[] parts = inputLine.split("--");
                System.out.println(parts[0]);

                switch (parts[0])
                {
                    case "AUTH":
                        getAuth(parts[1]);
                        break;
                    default:
                        out.println("Hello");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        finally {
            this.close();
        }
    }

    private void getAuth(String login)
    {
        ArrayList<String> logins = new ArrayList<>();
        logins.add("D1le");
        logins.add("Sink");
        logins.add("Zhl");
        logins.add("123");

        if(logins.contains(login))
        {
            out.println("AUTH--OK");
            out.flush();
        }
        else
        {
            out.println("AUTH--DENY");
        }
    }

    private void close()
    {
        try {
            in.close();
            out.close();
            clientSocket.close();
            System.out.println("Client disconnected");
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
