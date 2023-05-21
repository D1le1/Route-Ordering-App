package com.D1le;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Trip implements Serializable {

    private int id;
    private String route;
    private String startTime;
    private String endTime;
    private int seats;
    private String driverName;
    private String date;
    private int finished;

    public Trip(ResultSet rs, boolean operator) throws SQLException {
        id = rs.getInt("id");
        route = rs.getString("route");
        startTime = rs.getString("time");
        LocalTime time = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
        double hour = rs.getDouble("trip_time");
        int minute = (int) ((hour - (int) hour) * 60);
        time = time.plusHours((int) hour).plusMinutes(minute);
        endTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        if(!operator) {
            seats = 14 - rs.getInt("seats");
            driverName = "";
        }
        else
            driverName = rs.getString("name");
        date = rs.getString("date");
        finished = rs.getInt("finished");
    }

    public int getId() {
        return id;
    }

    public String getRoute() {
        return route;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDate() {
        return date;
    }

    public int getFinished() {
        return finished;
    }

    public int getSeats() {
        return seats;
    }
}