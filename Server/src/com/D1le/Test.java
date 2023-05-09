package com.D1le;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        double hour = 2.7;
        int minute = (int) ((hour - (int) hour) * 60);
        hour = (int) hour;
        System.out.println(hour + " " + minute);
    }
}