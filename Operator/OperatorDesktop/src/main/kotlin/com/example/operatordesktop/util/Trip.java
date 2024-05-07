package com.example.operatordesktop.util;

import java.io.Serializable;

public class Trip implements Serializable {

    private int id;
    private String route;
    private String startTime;
    private String endTime;
    private int seats;
    private String driverName;
    private String date;
    private int finished;

    public Trip(int id, String route, String startTime, String endTime, int seats, String driverName, String date, int finished) {
        this.id = id;
        this.route = route;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seats = seats;
        this.driverName = driverName;
        this.date = date;
        this.finished = finished;
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

    public String getDate() {
        return date;
    }

    public int getFinished() {
        return finished;
    }

    public void changeSeats(int number)
    {
        seats += number;
    }

    public String getDriverName() {
        return driverName;
    }
}