package com.D1le;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
                    case "TRIPS":
                        getTrips();
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

    private void getTrips()
    {
        List<Trip> mTrips = new ArrayList<>();
        mTrips.add(new Trip("Москва - Санкт-Петербург", "10:00"));
        mTrips.add(new Trip("Санкт-Петербург - Москва", "12:00"));
        mTrips.add(new Trip("Москва - Новосибирск", "14:00"));
        mTrips.add(new Trip("Новосибирск - Москва", "16:00"));
        mTrips.add(new Trip("Санкт-Петербург - Новосибирск", "18:00"));
        mTrips.add(new Trip("Новосибирск - Санкт-Петербург", "20:00"));

        JSONArray jsonArray = new JSONArray();
        for(Trip trip : mTrips)
        {
            JSONObject object = new JSONObject();
            object.put("route", trip.getRoute());
            object.put("time", trip.getTime());
            jsonArray.add(object);
        }

        out.println(jsonArray);
        out.flush();
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
