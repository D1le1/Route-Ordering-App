package com.example.busik.servertasks;

import com.example.busik.Trip;
import com.example.busik.client.Client;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    public static Client parseToClient(JSONObject obj) throws JSONException {
        return new Client(
                obj.getString("name"),
                obj.getString("address"),
                obj.getString("phone"),
                obj.getInt("id"),
                obj.getInt("role")
        );
    }

    public static Trip parseToTrip(JSONObject obj) throws JSONException {
        return new Trip(obj.getString("route"), obj.getString("time"));
    }
}
