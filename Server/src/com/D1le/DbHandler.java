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
        System.out.println("Database connected");
    }

    public Client getAuth(String login, String password)
    {
        try{
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select t1.role_id, t2.id, t2.name, t2.number" +
                    " from usersroles t1 inner join users t2" +
                    " on t1.user_id = t2.id where t2.number = " + login + " and t2.password = \"" + password + "\"");
            while (rs.next())
            {
                Client client = new Client(
                        rs.getString("name"),
                        "Nothing",
                        rs.getString("number"),
                        rs.getInt("id"),
                        rs.getInt("role_id")
                );
                rs.close();
                statement.close();
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
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select name, number, t1.id, arrived from users t1 inner join clientstrips t2 " +
                    "on t1.id = t2.user_id where t2.trip_id = " + tripId);
            List<Client> clients = new ArrayList<>();
            while (rs.next())
            {
                clients.add(new Client(
                        rs.getString("name"),
                        "Nothing",
                        rs.getInt("arrived"),
                        rs.getString("number"),
                        rs.getInt("id")
                ));
            }
            rs.close();
            statement.close();
            return clients;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<Trip> getDriverTrips(int driverId)
    {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT t1.id, t1.time, t2.time as trip_time, route, count(t3.trip_id) as seats " +
                    "FROM trips t1 " +
                    "INNER JOIN routes t2 ON t2.id = t1.route_id " +
                    "LEFT JOIN ClientsTrips t3 ON t3.trip_id = t1.id and arrived < 2 " +
                    "WHERE driver_id = " + driverId +
                    " GROUP BY t1.id");
            List<Trip> trips = new ArrayList<>();
            while (rs.next())
            {
                trips.add(new Trip(rs));
            }
            rs.close();
            statement.close();
            return trips;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<Trip> getSearchTrips(String start, String end, String date)
    {
        try{
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT t1.id, t1.time, t2.time as trip_time, route, count(t3.trip_id) as seats " +
                    "FROM trips t1 " +
                    "INNER JOIN routes t2 ON t2.id = t1.route_id " +
                    "LEFT JOIN ClientsTrips t3 ON t3.trip_id = t1.id and arrived < 2 " +
                    "WHERE route = '" + start + "-" + end + "' and date = '" + date + "' " +
                    "GROUP BY t1.id");
            List<Trip> trips = new ArrayList<>();
            while (rs.next())
            {
                trips.add(new Trip(rs));
            }
            rs.close();
            statement.close();
            return trips;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<Trip> getTrips() {
        try {
            statement = connection.createStatement();
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
            rs.close();
            statement.close();
            return trips;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setClientArrived(int tripId, int clientId, int status) throws SQLException
    {
        statement = connection.createStatement();
        statement.executeUpdate("update ClientsTrips set arrived = " + status +
                " where user_id = " + clientId + " and trip_id = " + tripId);
        statement.close();
    }
}
