package com.example.busik;

import com.example.busik.client.Client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Trip implements Serializable {

    private String mRoute;
    private String mTime;
    private boolean mArrived;
    private List<Client> mPassengerList;
    private String mDriver;
    private int mId;

    public Trip(String route, String time, int id) {
        mRoute = route;
        mTime = time;
        mArrived = false;
        mPassengerList = new ArrayList<>();
        mId = id;
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