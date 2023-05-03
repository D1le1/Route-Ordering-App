package com.example.busik.client;

import java.io.Serializable;

public class Client implements Serializable {

    private int mId;
    private String mName;
    private String mAddress;
    private String mPhone;
    private boolean mArrived;
    private int mRole;

    public Client(String name, String address, String phone, int id, int role) {
        mName = name;
        mAddress = address;
        mPhone = phone;
        mArrived = false;
        mRole = role;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public boolean hasArrived() {
        return mArrived;
    }

    public void setArrived(boolean arrived) {
        mArrived = arrived;
    }

    public int getRole() {
        return mRole;
    }

    public int getId() {
        return mId;
    }
}