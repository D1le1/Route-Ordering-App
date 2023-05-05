package com.D1le;

import java.net.*;
import java.io.*;
import java.sql.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8001);
            System.out.println("Server started.");

            DbHandler dbHandler = new DbHandler();
            dbHandler.connectToDb();

            while (true) {
                Socket socket = server.accept();
                new Thread(new ClientHandler(socket, dbHandler)).start();
                System.out.println("Client connected.");
            }
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}