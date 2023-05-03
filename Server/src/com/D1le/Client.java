package com.D1le;

public class Client {

    private int mId;
    private String mName;
    private String mAddress;
    private String mPhone;
    private int mRole;

    public Client(String name, String address, String phone, int id, int role) {
        mName = name;
        mAddress = address;
        mPhone = phone;
        mId = id;
        mRole = role;
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
        return mAddress;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public int getRole() {
        return mRole;
    }
}