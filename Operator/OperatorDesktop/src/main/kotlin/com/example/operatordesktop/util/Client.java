package com.example.operatordesktop.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client implements Serializable {
    private int mId;
    private String mName;
    private String address;
    private String mPhone;
    private int mRole;
    private int arrived;
    private List<String> bus;

    public Client(int id, String name, String phone, List<String> bus)
    {
        mName = name;
        mPhone = phone;
        this.bus = bus;
        mId = id;
    }

    public Client(String name, String address, String phone, int id, int role) {
        mName = name;
        this.address = address;
        mPhone = phone;
        mId = id;
        mRole = role;
    }

    public Client(String name, String address, String phone, int id, int role, int arrived) {
        mName = name;
        this.address = address;
        mPhone = phone;
        mId = id;
        mRole = role;
        this.arrived = arrived;
    }

    public Client(String name, String address, int arrived, String phone, int id) {
        mName = name;
        this.address = address;
        mPhone = phone;
        mId = id;
        this.arrived = arrived;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setArrived(int arrived) {
        this.arrived = arrived;
    }

    public int getArrived() {
        return arrived;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public List<String> getBus() {
        return bus;
    }

    public int getRole() {
        return mRole;
    }
}