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

    public Trip(String route, String startTime, int id) {}

    public Trip(int id, String route, String startTime, String endTime) {
        this.id = id;
        this.route = route;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Trip(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        route = rs.getString("route");
        startTime = rs.getString("time");
        LocalTime time = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
        double hour = rs.getDouble("trip_time");
        int minute = (int) ((hour - (int) hour) * 60);
        time = time.plusHours((int) hour).plusMinutes(minute);
        endTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        seats = 14 - rs.getInt("seats");
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

    public int getSeats() {
        return seats;
    }
}