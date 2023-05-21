package com.D1le;

import org.json.JSONObject;

public class MyJSONObject extends JSONObject {

    public MyJSONObject(Trip trip) {
        this.put("id", trip.getId());
        this.put("route", trip.getRoute());
        this.put("start", trip.getStartTime());
        this.put("end", trip.getEndTime());
        this.put("seats", trip.getSeats());
        this.put("name", trip.getDriverName());
        this.put("date", trip.getDate());
        this.put("finished", trip.getFinished());
    }

    public MyJSONObject(Client client) {
        this.put("id", client.getId());
        this.put("name", client.getName());
        this.put("address", client.getAddress());
        this.put("phone", client.getPhone());
        this.put("role", client.getRole());
        this.put("arrived", client.getArrived());
    }
}
