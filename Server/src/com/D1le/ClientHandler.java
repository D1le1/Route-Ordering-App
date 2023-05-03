package com.D1le;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private PrintWriter out;
    private Scanner in;

    private List<Client> mClients;
    private List<Trip> mTrips;
    private List<ClientRoutes> mClientRoutes;

    public ClientHandler(Socket clientSocket) {
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
                    case "TRIP":
                        getTripInfo();
                    default:
                        out.println("Nothing to send");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        finally {
            this.close();
        }
    }

    private void getTripInfo() {
        mTrips = new ArrayList<>();
        mTrips.add(new Trip("Москва - Санкт-Петербург", "10:00", 1));
        mTrips.add(new Trip("Санкт-Петербург - Москва", "12:00", 2));
        mTrips.add(new Trip("Москва - Новосибирск", "14:00", 3));
        mTrips.add(new Trip("Новосибирск - Москва", "16:00", 4));
        mTrips.add(new Trip("Санкт-Петербург - Новосибирск", "18:00", 5));
        mTrips.add(new Trip("Новосибирск - Санкт-Петербург", "20:00", 6));



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

    private void getAuth(String login)
    {
        ArrayList<String> logins = new ArrayList<>();
        logins.add("D1le");
        logins.add("Sink");
        logins.add("Zhl");
        logins.add("123");

        mClients = new ArrayList<>();
        mClients.add(new Client("Сергей Сергеев", "ул. Гагарина, д. 20, кв. 10", "+7 999 345-67-89", 11));
        mClients.add(new Client("Иван Иванов", "ул. Ленина, д. 5, кв. 3", "+7 999 234-56-78", 5));
        mClients.add(new Client("Петр Петров", "ул. Пушкина, д. 10, кв. 45", "+7 999 123-45-67", 7));
        mClients.add(new Client("Ольга Николаева", "ул. Маяковского, д. 7, кв. 25", "+7 999 456-78-90", 2));
        mClients.add(new Client("Елена Иванова", "ул. Кирова, д. 15, кв. 8", "+7 999 987-65-43", 12));
        mClients.add(new Client("Алексей Федоров", "ул. Советская, д. 30, кв. 12", "+7 999 876-54-32", 10));
        mClients.add(new Client("Наталья Смирнова", "ул. Гоголя, д. 17, кв. 7", "+7 999 567-89-01", 9));
        mClients.add(new Client("Дмитрий Козлов", "ул. Жуковского, д. 1, кв. 4", "+7 999 321-54-76", 8));
        mClients.add(new Client("Мария Ильина", "ул. Лермонтова, д. 12, кв. 19", "+7 999 654-32-10", 13));
        mClients.add(new Client("Светлана Тихомирова", "ул. Герцена, д. 8, кв. 6", "+7 999 765-43-21", 3));
        mClients.add(new Client("Андрей Белов", "ул. Калинина, д. 25, кв. 11", "+7 999 234-56-78", 14));
        mClients.add(new Client("Олег Денисов", "ул. Фрунзе, д. 14, кв. 21", "+7 999 765-43-21", 4));
        mClients.add(new Client("Татьяна Григорьева", "ул. Пушкина, д. 7, кв. 9", "+7 999 456-78-90", 6));
        mClients.add(new Client("Ирина Королева", "ул. Ломоносова, д. 3, кв. 1", "+7 999 234-56-78", 15));
        mClients.add(new Client("Анна Васильева", "ул. Мира, д. 9, кв. 16", "+7 999 123 222-33-33", 20));

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
        mTrips = new ArrayList<>();
        mTrips.add(new Trip("Москва - Санкт-Петербург", "10:00", 1));
        mTrips.add(new Trip("Санкт-Петербург - Москва", "12:00", 2));
        mTrips.add(new Trip("Москва - Новосибирск", "14:00", 3));
        mTrips.add(new Trip("Новосибирск - Москва", "16:00", 4));
        mTrips.add(new Trip("Санкт-Петербург - Новосибирск", "18:00", 5));
        mTrips.add(new Trip("Новосибирск - Санкт-Петербург", "20:00", 6));

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
