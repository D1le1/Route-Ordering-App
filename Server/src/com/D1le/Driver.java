package com.D1le;

public class Driver {

    private int mId;
    private String mName;
    private String mPhone;

    public Driver(String name, String phone, int id) {
        mName = name;
        mPhone = phone;
        mId = id;
    }

    public int getmId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPhone() {
        return mPhone;
    }
}