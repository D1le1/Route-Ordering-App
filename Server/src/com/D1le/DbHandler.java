package com.D1le;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
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
            ResultSet rs = statement.executeQuery("SELECT t1.id, t1.date, t1.time, t2.time as trip_time, route, count(t3.trip_id) as seats " +
                    "FROM trips t1 " +
                    "INNER JOIN routes t2 ON t2.id = t1.route_id " +
                    "LEFT JOIN ClientsTrips t3 ON t3.trip_id = t1.id and arrived < 2 " +
                    "WHERE driver_id = " + driverId +
                    " GROUP BY t1.id");
            List<Trip> trips = new ArrayList<>();
            while (rs.next())
            {
                trips.add(new Trip(rs, false));
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
            ResultSet rs = statement.executeQuery("SELECT t1.id, t1.date, t1.time, t2.time as trip_time, route, count(t3.trip_id) as seats " +
                    "FROM trips t1 " +
                    "INNER JOIN routes t2 ON t2.id = t1.route_id " +
                    "LEFT JOIN ClientsTrips t3 ON t3.trip_id = t1.id and arrived < 2 " +
                    "WHERE route = '" + start + "-" + end + "' and date = '" + date + "' " +
                    "GROUP BY t1.id");
            List<Trip> trips = new ArrayList<>();
            while (rs.next())
            {
                trips.add(new Trip(rs, false));
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

    public JSONObject getBookInfo(int tripId)
    {
        try {
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT u.name, u.number as phone, b.number, b.mark, b.color, r.cost, GROUP_CONCAT(s.name, ', ') AS stops_list\n" +
                    "FROM users u\n" +
                    "INNER JOIN Trips t ON u.id = t.driver_id\n" +
                    "INNER JOIN Routes r on r.id = t.route_id\n" +
                    "INNER JOIN Buses b ON b.driver_id = u.id\n" +
                    "INNER JOIN Stops s ON s.route_id = r.id\n" +
                    "WHERE t.id = " + tripId);
            JSONObject object = new JSONObject();
            while (rs.next())
            {
                object.put("name", rs.getString("name"));
                object.put("number", rs.getString("number"));
                object.put("phone", rs.getString("phone"));
                object.put("mark", rs.getString("mark"));
                object.put("color", rs.getString("color"));
                object.put("cost", rs.getString("cost"));
                object.put("stops", rs.getString("stops_list"));
            }
            statement.close();
            return object;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addNewUser(String login, String password, String name, int role) throws SQLException
    {
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from users where number = " + login);
        if(rs.next())
        {
            statement.close();
            return false;
        }
        statement.close();
        PreparedStatement statement;
        statement = connection.prepareStatement("INSERT into users (number, password, name) values (?,?,?)");
        statement.setString(1, login);
        statement.setString(2, password);
        statement.setString(3, name);
        statement.executeUpdate();

        statement.close();

        statement = connection.prepareStatement("INSERT into UsersRoles (user_id, role_id) SELECT id, ? from users where number = ?");
        statement.setInt(1, role);
        statement.setString(2, login);
        statement.executeUpdate();

        statement.close();
        return true;
    }

    public List<Trip> getTrips() {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT t1.id, t1.date, t1.time, t2.time as trip_time, route, u.name " +
                    "FROM trips t1 " +
                    "INNER JOIN routes t2 ON t2.id = t1.route_id " +
                    "INNER JOIN users u on t1.driver_id = u.id " +
                    "GROUP BY t1.id");
            List<Trip> trips = new ArrayList<>();
            while (rs.next())
            {
                trips.add(new Trip(rs, true));
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
