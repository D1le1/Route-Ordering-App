package com.D1le;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private DbHandler mDbHandler;
    private PrintWriter out;
    private Scanner in;

    public ClientHandler(Socket clientSocket, DbHandler dbHandler) {
        this.clientSocket = clientSocket;
        mDbHandler = dbHandler;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new Scanner(clientSocket.getInputStream());

            String inputLine;
            while (null != (inputLine = in.nextLine())) {
                System.out.println("Receive: " + inputLine);
                String[] parts = inputLine.split("--");
                if (parts.length < 2) {
                    out.println("Nothing to send");
                    continue;
                }
                switch (parts[0]) {
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
                    case "INFO":
                        getBookInfo(Integer.parseInt(parts[1]));
                        break;
                    case "HISTORY":
                        getHistoryInfo(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        break;
                    case "MARK":
                        setClientArrived(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                        break;
                    case "SEARCH":
                        getTrips(parts[1], parts[2], parts[3]);
                        break;
                    case "CONFIRM":
                        confirmOrder(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);
                        break;
                    case "DRIVERS":
                        getDrivers(Integer.parseInt(parts[1]));
                        break;
                    case "BUSES":
                        getBuses(Integer.parseInt(parts[1]));
                        break;
                    case "APPLICATION":
                        if (parts[1].equals("LIST"))
                            getApplicationList();
                        else
                            changeApplication(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        break;
                    case "FINISH":
                        finishTrip(parts[1], parts[2]);
                        break;
                    case "ADD":
                        addInfo(parts);
                        break;
                    case "UPDATE":
                        updateInfo(parts);
                        break;
                    case "DELETE":
                        deleteInfo(parts);
                        break;
                    default:
                        out.println("Nothing to send");
                        out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            this.close();
        }
    }

    private void finishTrip(String status, String tripId) {
        try {
            mDbHandler.finishTrip(status, tripId);
            out.println("FINISH--DENY");
        }catch (SQLException e){
            out.println("FINISH--DENY");
        }finally {
            out.flush();
        }

    }

    private void getBookInfo(int tripId) {
        JSONObject object = mDbHandler.getBookInfo(tripId);
        if (object != null)
            out.println(object);
        else
            out.println("INFO--DENY");
        out.flush();
    }

    private void getHistoryInfo(int userId, int tripId) {
        JSONObject object = mDbHandler.getHistoryInfo(userId, tripId);
        if (object != null) {
            out.println(object);
        } else
            out.println("HISTORY--DENY");
        out.flush();
    }

    private void registration(String login, String password, String name, int role) {
        try {
            if (mDbHandler.addNewUser(login, password, name, role))
                out.println("REG--OK");
            else
                out.println("REG--EXISTS");
        } catch (SQLException e) {
            out.println("REG--DENY");
            e.printStackTrace();
        }
        out.flush();
    }

    private void setClientArrived(int tripId, int clientId, int status) {
        try {
            mDbHandler.setClientArrived(tripId, clientId, status);
            out.println("MARK--" + status);
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("MARK--DENY");
        }
        out.flush();
    }

    private void getTripInfo(int tripId) {
        JSONArray jsonArray = new JSONArray();
        List<Client> clients;
        clients = mDbHandler.getTripInfo(tripId);
        for (Client client : clients) {
            MyJSONObject object = new MyJSONObject(client);
            jsonArray.put(object);
        }
        out.println(jsonArray);
        out.flush();
    }

    private void confirmOrder(int userId, int tripId, String stop) {
        if (mDbHandler.confirmOrder(userId, tripId, stop)) {
            out.println("CONFIRM--OK");
        } else
            out.println("CONFIRM--DENY");
        out.flush();
    }

    private void getAuth(String login, String password) {
        MyJSONObject object = mDbHandler.getAuth(login, password);
        if (object != null) {
            out.println(object);
        } else {
            out.println("AUTH--DENY");
        }
        out.flush();
    }

    private void getTrips(String start, String end, String date) {
        JSONArray jsonArray = new JSONArray();
        List<Trip> trips = mDbHandler.getSearchTrips(start, end, date);
        for (Trip trip : trips) {
            MyJSONObject object = new MyJSONObject(trip);
            jsonArray.put(object);
        }
        out.println(jsonArray);
        out.flush();
    }

    private void getTrips(int id, int role) {
        JSONArray jsonArray;
        if (role == 1) {
            jsonArray = mDbHandler.getHistoryTrips(id);
        } else if (role == 2) {
            jsonArray = mDbHandler.getDriverTrips(id);
        } else {
            jsonArray = mDbHandler.getTrips();
        }
        out.println(jsonArray);
        out.flush();
    }

    private void getDrivers(int manage) {
        JSONArray jsonArray = mDbHandler.getDrivers(manage);
        out.println(jsonArray);
        out.flush();
    }

    private void getBuses(int manage) {
        JSONArray jsonArray = mDbHandler.getBuses(manage);
        out.println(jsonArray);
        out.flush();
    }

    private void getApplicationList() {
        JSONArray jsonArray = mDbHandler.getApplicationList();
        out.println(jsonArray);
        out.flush();
    }

    private void changeApplication(int clientId, int choose) {
        try {
            mDbHandler.changeApplicationStatus(clientId, choose);
            out.println("APPLICATION--OK");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("APPLICATION--DENY");
        }
        out.flush();
    }

    private void addInfo(String[] parts) {
        try {
            switch (parts[1]) {
                case "TRIP":
                    if (!mDbHandler.addTrip(parts[2], parts[3], parts[4], Integer.parseInt(parts[5]))) {
                        out.println("ADD--DENY");
                        return;
                    }
                    break;
                case "BUS":
                    if (!mDbHandler.addBus(parts[2], parts[3], parts[4], Integer.parseInt(parts[5]))) {
                        out.println("ADD--DENY");
                        return;
                    }
                    break;
            }
            out.println("ADD--OK");
        } catch (SQLException e) {
            out.println("ADD--DENY");
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }

    private void updateInfo(String[] parts) {
        try {
            switch (parts[1]) {
                case "TRIP":
                    if (!mDbHandler.updateTrip(parts[2], parts[3], parts[4], Integer.parseInt(parts[5]), Integer.parseInt(parts[6]))) {
                        out.println("UPDATE--DENY");
                        return;
                    }
                    break;
                case "BUS":
                    if(!mDbHandler.updateBus(Integer.parseInt(parts[2]), parts[3], parts[4], parts[5], Integer.parseInt(parts[6]))) {
                        out.println("UPDATE--DENY");
                        return;
                    }
                    break;
                case "DRIVER":
                    if(!mDbHandler.updateDriver(parts[2], parts[3], Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6]))){
                        out.println("UPDATE--DENY");
                        return;
                    }
                    break;
            }
            out.println("UPDATE--OK");
        } catch (SQLException e) {
            System.out.println("123");
            out.println("UPDATE--DENY");
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }

    private void deleteInfo(String[] parts) {
        try {
            switch (parts[1]) {

                case "ORDER":
                    mDbHandler.deleteOrder(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                    break;
                case "TRIP":
                    mDbHandler.deleteTrip(Integer.parseInt(parts[2]));
                    break;
                case "BUS":
                    mDbHandler.deleteBus(Integer.parseInt(parts[2]));
                    break;
            }
            out.println("DELETE--OK");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("DELETE--DENY");
        } finally {
            out.flush();
        }
    }

    private void close() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            System.out.println("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
