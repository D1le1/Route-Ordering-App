package com.D1le;

import org.json.JSONObject;

public class MyJSONObject extends JSONObject {

    public MyJSONObject(Trip trip) {
        this.put("route", trip.getRoute());
        this.put("time", trip.getTime());
        this.put("id", trip.getId());
    }

    public MyJSONObject(Client client) {
        this.put("id", client.getId());
        this.put("name", client.getName());
        this.put("address", client.getAddress());
        this.put("phone", client.getPhone());
        this.put("role", client.getRole());
        this.put("arrived", client.getArrived());
    }

    public Client parseToClient(){
        return new Client(
                this.getString("name"),
                this.getString("address"),
                this.getString("phone"),
                this.getInt("id"),
                this.getInt("role")
        );
    }

    public Trip parseToTrip() {
        return new Trip(this.getString("route"), this.getString("time"), this.getInt("id"));
    }
}
