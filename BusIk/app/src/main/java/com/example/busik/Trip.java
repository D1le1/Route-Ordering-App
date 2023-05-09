package com.example.busik;

import java.io.Serializable;

public class Trip implements Serializable {

    private int id;
    private String route;
    private String startTime;
    private String endTime;
    private int seats;

    public Trip(String route, String startTime, int id) {}

    public Trip(int id, String route, String startTime, String endTime, int seats) {
        this.id = id;
        this.route = route;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seats = seats;
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

    public void changeSeats(int number)
    {
        seats += number;
    }
}