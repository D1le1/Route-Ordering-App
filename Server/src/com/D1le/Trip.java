package com.D1le;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Trip implements Serializable {

    private int mId;
    private String mRoute;
    private String mTime;
    private boolean mArrived;
    private List<Client> mPassengerList;
    private String mDriver;

    public Trip(String route, String time, int id) {
        mRoute = route;
        mTime = time;
        mArrived = false;
        mPassengerList = new ArrayList<>();
        mId = id;
    }

    public Trip(ResultSet rs) throws SQLException {
        mId = rs.getInt("id");
        mRoute = rs.getString("route");
        mTime = rs.getString("time");
    }

    public int getId() {
        return mId;
    }

    public String getRoute() {
        return mRoute;
    }

    public String getTime() {
        return mTime;
    }

    public String getDriver() {return mDriver; }

    public boolean isArrived() {
        return mArrived;
    }

    public void setArrived(boolean arrived) {
        mArrived = arrived;
    }

    public List<Client> getPassengerList() {
        return mPassengerList;
    }

    public int getPassengersCount()
    {
        return mPassengerList.size();
    }

    public void addPassenger(Client client) {
        mPassengerList.add(client);
    }

    public void removePassenger(Client client) {
        mPassengerList.remove(client);
    }
}