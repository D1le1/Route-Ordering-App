package com.D1le;

import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbHandler {
    private static final String CON_STR = "jdbc:sqlite:database.db";
    private Connection connection = null;
    private Statement statement = null;

    public void connectToDb() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(CON_STR);
        statement = connection.createStatement();
        System.out.println("Database connected");
    }

    public Client getAuth(String login)
    {
        try{
            ResultSet rs = statement.executeQuery("Select t1.role_id, t2.id, t2.name, t2.number" +
                    " from usersroles t1 inner join users t2" +
                    " on t1.user_id = t2.id where t2.number = " + login);
            while (rs.next())
            {
                Client client = new Client(
                        rs.getString("name"),
                        "Nothing",
                        rs.getString("number"),
                        rs.getInt("id"),
                        rs.getInt("role_id")
                );
                return client;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<Client> getTripInfo(int tripId)
    {
        try{
            ResultSet rs = statement.executeQuery("Select name, number  from users t1 inner join clientstrips t2 " +
                    "on t1.id = t2.user_id where t2.trip_id = " + tripId);
            List<Client> clients = new ArrayList<>();
            while (rs.next())
            {
                clients.add(new Client(
                        rs.getString("name"),
                        "Nothing",
                        rs.getString("number")
                ));
                return clients;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<Trip> getDriverTrips(int driverId)
    {
        try {
            ResultSet rs = statement.executeQuery("Select * from trips where driver_id = " + driverId);
            List<Trip> trips = new ArrayList<>();
            while (rs.next())
            {
                trips.add(new Trip(
                        rs.getString("route"),
                        rs.getString("time"),
                        rs.getInt("id")
                ));
            }
            return trips;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public List<Trip> getTrips() {
        try {
            ResultSet rs = statement.executeQuery("Select * from trips");
            List<Trip> trips = new ArrayList<>();
            while (rs.next())
            {
                trips.add(new Trip(
                        rs.getString("route"),
                        rs.getString("time"),
                        rs.getInt("id")
                ));
            }
            return trips;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
