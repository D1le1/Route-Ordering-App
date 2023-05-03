package com.D1le;

public class Client {

    private int mId;
    private String mName;
    private String mAddress;
    private String mPhone;
    private boolean mArrived;

    public Client(String name, String address, String phone, int id) {
        mName = name;
        mAddress = address;
        mPhone = phone;
        mArrived = false;
        mId = id;
    }

    public int getmId() {
        return mId;
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
}