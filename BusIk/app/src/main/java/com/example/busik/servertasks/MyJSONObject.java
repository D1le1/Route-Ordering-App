package com.example.busik.servertasks;

import com.example.busik.Trip;
import com.example.busik.client.Client;

import org.json.JSONException;
import org.json.JSONObject;

public class MyJSONObject extends JSONObject {

    public MyJSONObject(Trip trip) throws JSONException {
        this.put("route", trip.getRoute());
        this.put("time", trip.getTime());
        this.put("id", trip.getId());
    }

    public MyJSONObject(Client client) throws JSONException {
        this.put("id", client.getId());
        this.put("name", client.getName());
        this.put("address", client.getAddress());
        this.put("phone", client.getPhone());
        this.put("role", client.getRole());
    }

    public MyJSONObject(String response) throws JSONException {
        super(response);
    }

    public MyJSONObject(JSONObject object) throws JSONException {
        super(object.toString());
    }

    public Client parseToClient() throws JSONException {
        return new Client(
                this.getString("name"),
                this.getString("address"),
                this.getString("phone"),
                this.getInt("id"),
                this.getInt("role"),
                this.getInt("arrived")
        );
    }

    public Trip parseToTrip() throws JSONException {
        return new Trip(this.getString("route"), this.getString("time"), this.getInt("id"));
    }
}
