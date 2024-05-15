package com.D1le;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public MyJSONObject getAuth(String login, String password) {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select t1.role_id, t2.id, t2.name, t2.number, t2.apply" +
                    " from usersroles t1 inner join users t2" +
                    " on t1.user_id = t2.id where t2.number = " + login + " and t2.password = \"" + password + "\"");
            while (rs.next()) {
                Client client = new Client(
                        rs.getString("name"),
                        "Nothing",
                        rs.getString("number"),
                        rs.getInt("id"),
                        rs.getInt("role_id")
                );
                MyJSONObject object = new MyJSONObject(client);
                object.put("apply", rs.getInt("apply"));
                rs.close();
                statement.close();
                return object;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Client> getTripInfo(int tripId) {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select u.name, u.number, ct.arrived, u.id, s.name as stop from users u\n" +
                    "inner join ClientsTrips ct on u.id = ct.user_id\n" +
                    "inner join Stops s on s.id = ct.stop_id\n" +
                    "where trip_id = " + tripId);
            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                clients.add(new Client(
                        rs.getString("name"),
                        rs.getString("stop"),
                        rs.getInt("arrived"),
                        rs.getString("number"),
                        rs.getInt("id")
                ));
            }
            rs.close();
            statement.close();
            return clients;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getDriverTrips(int driverId) {
        try {
            JSONArray jsonArray = new JSONArray();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT t1.id, t1.date, t1.time, t2.time as trip_time, finished, route, count(t3.trip_id) as seats " +
                    "FROM trips t1 " +
                    "INNER JOIN routes t2 ON t2.id = t1.route_id " +
                    "LEFT JOIN ClientsTrips t3 ON t3.trip_id = t1.id and arrived < 2 " +
                    "WHERE driver_id = " + driverId + " and t1.finished = 0" +
                    " GROUP BY t1.id");
            while (rs.next()) {
                MyJSONObject object = new MyJSONObject(new Trip(rs, false));
                jsonArray.put(object);
            }
            rs.close();
            statement.close();
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getHistoryTrips(int clientId) {
        try {
            JSONArray jsonArray = new JSONArray();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT t.id, t.date, t.time, r.time as trip_time, route, finished, arrived, name\n" +
                    "FROM ClientsTrips ct \n" +
                    "INNER JOIN Routes r ON r.id = t.route_id\n" +
                    "INNER JOIN Trips t on ct.trip_id = t.id\n" +
                    "INNER JOIN Users u on u.id = t.driver_id\n" +
                    "WHERE ct.user_id = " + clientId + "\n" +
                    "ORDER BY ct.id DESC");
            while (rs.next()) {
                MyJSONObject object = new MyJSONObject(new Trip(rs, true));
                object.put("arrived", rs.getInt("arrived"));
                jsonArray.put(object);
            }
            rs.close();
            statement.close();
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getHistoryInfo(int clientId, int tripId) {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select s.name as stop, u.name, u.number as phone, b.mark, b.number, b.color, r.cost\n" +
                    "from stops s\n" +
                    "INNER join ClientsTrips ct on s.id = ct.stop_id\n" +
                    "INNER Join Trips t on t.id = ct.trip_id\n" +
                    "Inner join Users u on u.id = t.driver_id\n" +
                    "Inner join Buses b on b.driver_id = t.driver_id\n" +
                    "Inner Join Routes r on r.id = t.route_id\n" +
                    "where ct.user_id = " + clientId + " and t.id = " + tripId);
            JSONObject object = new JSONObject();
            while (rs.next()) {
                object.put("name", rs.getString("name"));
                object.put("number", rs.getString("number"));
                object.put("phone", rs.getString("phone"));
                object.put("mark", rs.getString("mark"));
                object.put("color", rs.getString("color"));
                object.put("cost", rs.getString("cost"));
                object.put("stop", rs.getString("stop"));
            }
            rs.close();
            statement.close();
            return object;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Trip> getSearchTrips(String start, String end, String date) {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT t1.id, t1.date, t1.time, t1.finished, t2.time as trip_time, route, count(t3.trip_id) as seats " +
                    "FROM trips t1 " +
                    "INNER JOIN routes t2 ON t2.id = t1.route_id " +
                    "LEFT JOIN ClientsTrips t3 ON t3.trip_id = t1.id and arrived < 2 " +
                    "WHERE route = '" + start + "-" + end + "' and date = '" + date + "' " + "and t1.finished = 0 " +
                    "GROUP BY t1.id");
            List<Trip> trips = new ArrayList<>();
            while (rs.next()) {
                trips.add(new Trip(rs, false));
            }
            rs.close();
            statement.close();
            return trips;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean confirmOrder(int userId, int tripId, String stop) {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * from clientstrips where user_id = " + userId + " and trip_id = " + tripId);
            if (rs.next()) {
                statement.close();
                return false;
            }
            statement.close();

            statement = connection.createStatement();
            rs = statement.executeQuery("select id from stops where name = '" + stop + "'");
            int stopId = 0;
            while (rs.next()) {
                stopId = rs.getInt("id");
            }
            statement.close();

            PreparedStatement statement = connection.prepareStatement("insert into ClientsTrips (user_id, trip_id, arrived, stop_id) VALUES (?, ?, 0, ?)");
            statement.setInt(1, userId);
            statement.setInt(2, tripId);
            statement.setInt(3, stopId);
            statement.executeUpdate();
            statement.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject getBookInfo(int tripId) {
        try {
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT u.name, u.number AS phone, b.number, b.mark, b.color, r.cost,\n" +
                    "(SELECT GROUP_CONCAT(sorted.name) FROM (SELECT s.name FROM Stops s WHERE s.route_id = r.id ORDER BY s.'order' ASC)" +
                    " AS sorted) AS stops_list\n" +
                    "FROM users u\n" +
                    "INNER JOIN Trips t ON u.id = t.driver_id\n" +
                    "INNER JOIN Routes r ON r.id = t.route_id\n" +
                    "INNER JOIN Buses b ON b.driver_id = u.id\n" +
                    "WHERE t.id = " + tripId);
            JSONObject object = new JSONObject();

            while (rs.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addNewUser(String login, String password, String name, int role) throws SQLException {
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from users where number = " + login);
        if (rs.next()) {
            statement.close();
            return false;
        }
        statement.close();
        PreparedStatement statement;
        statement = connection.prepareStatement("INSERT into users (number, password, name, apply) values (?,?,?, ?)");
        statement.setString(1, login);
        statement.setString(2, password);
        statement.setString(3, name);
        statement.setString(4, role == 1 ? "1" : null);
        statement.executeUpdate();

        statement.close();

        statement = connection.prepareStatement("INSERT into UsersRoles (user_id, role_id) SELECT id, ? from users where number = ?");
        statement.setInt(1, role);
        statement.setString(2, login);
        statement.executeUpdate();

        statement.close();
        return true;
    }

    public JSONArray getTrips() {
        try {
            JSONArray jsonArray = new JSONArray();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT t1.id, t1.date, t1.time, finished, t2.time as trip_time, route, u.name, u.id as driver_id, b.mark " +
                    "FROM trips t1 " +
                    "INNER JOIN routes t2 ON t2.id = t1.route_id " +
                    "LEFT JOIN users u on t1.driver_id = u.id " +
                    "LEFT JOIN buses b on u.id = b.driver_id " +
                    "WHERE t1.finished = 0 " +
                    "GROUP BY t1.id");
            while (rs.next()) {
                MyJSONObject object = new MyJSONObject(new Trip(rs, true));
                object.put("driver_name", rs.getString("name"));
                object.put("driver_id", rs.getInt("driver_id"));
                object.put("mark", rs.getString("mark"));
                jsonArray.put(object);
            }
            rs.close();
            statement.close();
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setClientArrived(int tripId, int clientId, int status) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate("update ClientsTrips set arrived = " + status +
                " where user_id = " + clientId + " and trip_id = " + tripId);
        statement.close();
    }

    public JSONArray getDrivers(int manage) {
        try {
            JSONArray jsonArray = new JSONArray();
            statement = connection.createStatement();
            ResultSet rs;
            if (manage == 1) {
                rs = statement.executeQuery("Select u.id, b.id as bus_id, b.mark, b.color, b.number, u.name, u.number as phone from Users u\n" +
                        "INNER JOIN Buses b on u.id = b.driver_id\n");
            } else if (manage == 2) {
                rs = statement.executeQuery("Select u.id, b.id as bus_id, b.mark, b.color, b.number, u.name, u.number as phone from Users u\n" +
                        "                        LEFT JOIN Buses b on u.id = b.driver_id\n" +
                        "                        INNER JOIN UsersRoles ur on ur.user_id = u.id\n" +
                        "                        where ur.role_id = 2 and mark is null");
            } else {
                rs = statement.executeQuery("Select u.id, b.id as bus_id, b.mark, b.color, b.number, u.name, u.number as phone from Users u\n" +
                        "LEFT JOIN Buses b on u.id = b.driver_id\n" +
                        "INNER JOIN UsersRoles ur on ur.user_id = u.id\n" +
                        "where ur.role_id = 2\n");
            }
            while (rs.next()) {
                JSONObject object = new JSONObject();
                object.put("id", rs.getInt("id"));
                object.put("name", rs.getString("name"));
                object.put("phone", rs.getString("phone"));
                if (rs.getString("mark") != null) {
                    object.put("mark", rs.getString("mark"));
                    object.put("color", rs.getString("color"));
                    object.put("number", rs.getString("number"));
                    object.put("bus_id", rs.getString("bus_id"));
                }
                jsonArray.put(object);
            }
            rs.close();
            statement.close();
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getBuses(int manage) {
        try {
            JSONArray jsonArray = new JSONArray();
            statement = connection.createStatement();
            ResultSet rs;
            if (manage == 1) {
                rs = statement.executeQuery("select u.name, b.id, b.driver_id, b.mark, b.color, b.number from Buses b\n" +
                        "Inner JOIN Users u on b.driver_id = u.id");
            } else if (manage == 2) {
                rs = statement.executeQuery("select u.name, b.id, b.driver_id, b.mark, b.color, b.number from Buses b\n" +
                        "                        LEFT JOIN Users u on b.driver_id = u.id\n" +
                        "where name is null");
            } else {
                rs = statement.executeQuery("select u.name, b.driver_id, b.id, b.mark, b.color, b.number from Buses b\n" +
                        "LEFT JOIN Users u on b.driver_id = u.id");
            }
            while (rs.next()) {
                JSONObject object = new JSONObject();
                object.put("id", rs.getInt("id"));
                object.put("driver_id", rs.getInt("driver_id"));
                object.put("mark", rs.getString("mark"));
                object.put("color", rs.getString("color"));
                object.put("number", rs.getString("number"));
                object.put("name", rs.getString("name"));
                jsonArray.put(object);
            }
            rs.close();
            statement.close();
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getApplicationList() {
        try {
            JSONArray jsonArray = new JSONArray();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select u.name, u.number, u.id, cr.role_id from Users u\n" +
                    "Inner Join UsersRoles cr on cr.user_id = u.id\n" +
                    "where u.apply is null");
            while (rs.next()) {
                JSONObject object = new JSONObject();
                object.put("name", rs.getString("name"));
                object.put("phone", rs.getString("number"));
                object.put("id", rs.getInt("id"));
                object.put("role", rs.getInt("role_id"));
                jsonArray.put(object);
            }
            rs.close();
            statement.close();
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void changeApplicationStatus(int clientId, int choose) throws SQLException {
        statement = connection.createStatement();
        if (choose == 1)
            statement.executeUpdate("Update Users set apply = 1 where id = " + clientId);
        else {
            statement.executeUpdate("Delete from UsersRoles where user_id = " + clientId);
            statement.executeUpdate("Delete from Users where id = " + clientId);
        }
        statement.close();
    }

    public boolean addTrip(String route, String date, String time, int driverId) throws SQLException {
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * FROM trips t\n" +
                "where t.date = \"" + date + "\" and t.time = \"" + time + "\" \n" +
                "and t.route_id = (select id from Routes where route = \"" + route + "\") and driver_id = " + driverId);
        if (rs.next()) {
            rs.close();
            statement.close();
            return false;
        }
        rs.close();
        statement.close();
        PreparedStatement statement = connection.prepareStatement("insert into Trips (route_id, driver_id, time, date, finished) VALUES (" +
                "(select id from Routes where route = \"" + route + "\"), ?, ?, ?, 0)");
        statement.setInt(1, driverId);
        statement.setString(2, time);
        statement.setString(3, date);
        statement.executeUpdate();
        statement.close();
        return true;
    }

    public boolean addBus(String mark, String number, String color, int driverId) throws SQLException {
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from buses where mark = \"" + mark + "\" and number = \"" + number + "\"");
        if (rs.next()) {
            rs.close();
            statement.close();
            return false;
        }
        rs.close();
        statement.close();
        PreparedStatement statement = connection.prepareStatement("insert into Buses (mark, number, color, driver_id) values (?, ?, ?, ?)");
        statement.setString(1, mark);
        statement.setString(2, number);
        statement.setString(3, color);
        statement.setInt(4, driverId);
        statement.executeUpdate();
        statement.close();
        return true;
    }

    public boolean updateTrip(String route, String date, String time, int driverId, int tripId) throws SQLException {
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * FROM trips t\n" +
                "where t.date = \"" + date + "\" and t.time = \"" + time + "\" \n" +
                "and t.route_id = (select id from Routes where route = \"" + route + "\") and driver_id = " + driverId);
        if (rs.next()) {
            rs.close();
            statement.close();
            return false;
        }
        rs.close();
        statement.close();
        statement = connection.createStatement();
        statement.executeUpdate("Update trips \n" +
                "set time = \"" + time + "\",\n" +
                "date = \"" + date + "\",\n" +
                "route_id = (select id from Routes where route = \"" + route + "\"),\n" +
                "driver_id = " + driverId + "\n" +
                "where id = " + tripId);
        statement.close();

        return true;
    }

    public Boolean updateBus(int driverId, String mark, String number, String color, int busId) throws SQLException {
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from buses where mark = \"" + mark + "\" and number = \"" + number + "\" and color = \"" + color + "\" and driver_id = " + driverId);
        if (rs.next()) {
            rs.close();
            statement.close();
            return false;
        }
        rs.close();
        statement.close();
        statement = connection.createStatement();
        statement.executeUpdate("Update buses \n" +
                "set driver_id = " + driverId + ",\n" +
                "mark = \"" + mark + "\",\n" +
                "number = \"" + number + "\",\n" +
                "color = \"" + color + "\"\n" +
                "where id = " + busId);
        statement.close();

        return true;
    }

    public Boolean updateDriver(String name, String phone, int driverId, int busId, int currentBusId) throws SQLException {
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from users where name = \"" + name + "\" and number = \"" + phone + "\"");
        if (rs.next() && busId == currentBusId) {
            rs.close();
            statement.close();
            return false;
        }
        rs.close();
        statement.close();
        statement = connection.createStatement();
        statement.executeUpdate("Update Users set name = \"" + name + "\", number = \"" + phone + "\" where id = " + driverId);
        statement.executeUpdate("Update Buses set driver_id = null where id = " + currentBusId);
        statement.executeUpdate("Update Buses set driver_id = " + driverId + " where id = " + busId);
        statement.close();

        return true;
    }

    public void deleteOrder(int clientId, int tripId) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate("delete from ClientsTrips where user_id = " + clientId + " and trip_id = " + tripId);
        statement.close();
    }

    public void deleteTrip(int tripId) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate("delete from Trips where id = " + tripId);
        statement.executeUpdate("delete from ClientsTrips where trip_id =" + tripId);
        statement.close();
    }

    public void deleteBus(int busId) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate("delete from Buses where id = " + busId);
        statement.close();
    }
}
