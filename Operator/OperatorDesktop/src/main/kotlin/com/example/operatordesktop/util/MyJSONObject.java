package com.example.operatordesktop.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public Client parseToApplyClient() throws  JSONException {
        return new Client(
                this.getString("name"),
                this.getString("phone"),
                this.getInt("id"),
                this.getInt("role")
        );
    }

    public Trip parseToTrip() throws JSONException {
        return new Trip(
                this.getInt("id"),
                this.getString("route"),
                this.getString("start"),
                this.getString("end"),
                this.getInt("seats"),
                this.has("name") ? this.getString("name") : "Нет закрепленного водителя",
                this.getString("date"),
                this.getInt("finished")
        );
    }

    public Client parseToDriver() throws JSONException {
        List<String> bus = new ArrayList<>();
        if (this.has("mark"))
            bus = Arrays.asList(this.getString("color"), this.getString("mark"), this.getString("number"), this.getString("bus_id"));
        else
            bus.add("Нет закрепленного водителя");
        return new Client(
                this.getInt("id"),
                this.getString("name"),
                this.getString("phone"),
                bus
        );
    }
}
