package com.D1le;

import org.json.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final DbHandler dbHandler;
    private PrintWriter out;
    private Scanner in;

    public ClientHandler(Socket clientSocket, DbHandler dbHandler) {
        this.clientSocket = clientSocket;
        this.dbHandler = dbHandler;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new Scanner(clientSocket.getInputStream());

            String inputLine;
            while (null != (inputLine = in.nextLine())) {
                System.out.println("Receive: " + inputLine);
                String[] parts = inputLine.split("--");
                if(parts.length < 2) {
                    out.println("Nothing to send");
                    continue;
                }

                switch (parts[0])
                {
                    case "AUTH":
                        getAuth(parts[1], parts[2]);
                        break;
                    case "REG":
                        registration(parts[1], parts[2], parts[3], Integer.parseInt(parts[4]));
                        break;
                    case "TRIPS":
                        getTrips(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        break;
                    case "TRIP":
                        getTripInfo(Integer.parseInt(parts[1]));
                        break;
                    case "MARK":
                        setClientArrived(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                        break;
                    case "SEARCH":
                        getTrips(parts[1], parts[2], parts[3]);
                        break;
                    default:
                        out.println("Nothing to send");
                        out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        finally {
            this.close();
        }
    }

    private void registration(String login, String password, String name, int role) {
        try {
            dbHandler.addNewUser(login, password, name, role);
            out.println("REG--OK");
            System.out.println("Here");
        }catch (SQLException e)
        {
            out.println("REG--DENY");
            e.printStackTrace();
        }
        out.flush();
    }

    private void setClientArrived(int tripId, int clientId, int status) {
        try {
            dbHandler.setClientArrived(tripId, clientId, status);
            out.println("MARK--" + status);
        }catch (SQLException e)
        {
            e.printStackTrace();
            out.println("MARK--DENY");
        }
        out.flush();
    }

    private void getTripInfo(int tripId) {
        JSONArray jsonArray = new JSONArray();
        List<Client> clients;
        clients = dbHandler.getTripInfo(tripId);
        for(Client client : clients)
        {
            MyJSONObject object = new MyJSONObject(client);
            jsonArray.put(object);
        }
        out.println(jsonArray);
        out.flush();
    }

    private void getAuth(String login, String password)
    {
        Client client = dbHandler.getAuth(login, password);
        if(client != null) {
            MyJSONObject object = new MyJSONObject(client);
            out.println(object);
        }
        else
        {
            out.println("AUTH--DENY");
        }
        out.flush();
    }

    private void getTrips(String start, String end, String date)
    {
        JSONArray jsonArray = new JSONArray();
        List<Trip> trips = dbHandler.getSearchTrips(start, end, date);
        for (Trip trip : trips)
        {
            MyJSONObject object = new MyJSONObject(trip);
            jsonArray.put(object);
        }
        out.println(jsonArray);
        out.flush();
    }

    private void getTrips(int id, int role)
    {
        JSONArray jsonArray = new JSONArray();
        List<Trip> trips;
        if(role == 2)
        {
            trips = dbHandler.getDriverTrips(id);
        }
        else
        {
            trips = dbHandler.getTrips();
        }
        for(Trip trip : trips)
        {
            MyJSONObject object = new MyJSONObject(trip);
            jsonArray.put(object);
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
