package com.example.busik.servertasks;

import com.example.busik.other.Trip;
import com.example.busik.client.Client;

import org.json.JSONException;
import org.json.JSONObject;

public class MyJSONObject extends JSONObject {

    public MyJSONObject(Trip trip) throws JSONException {
        this.put("id", trip.getId());
        this.put("route", trip.getRoute());
        this.put("start", trip.getStartTime());
        this.put("end", trip.getEndTime());
    }

    public MyJSONObject(Client client) throws JSONException {
        this.put("id", client.getId());
        this.put("name", client.getName());
        this.put("address", client.getAddress());
        this.put("phone", client.getPhone());
        this.put("role", client.getRole());
        this.put("arrived", client.getArrived());
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
        return new Trip(
                this.getInt("id"),
                this.getString("route"),
                this.getString("start"),
                this.getString("end"),
                this.getInt("seats"),
                this.getString("name"),
                this.getString("date")
        );
    }
}
