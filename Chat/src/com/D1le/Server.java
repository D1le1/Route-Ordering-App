package com.D1le;

import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8001);
            System.out.println("Server started.");

            while (true) {
                Socket socket = server.accept();
                new Thread(new ClientHandler2(socket)).start();
                System.out.println("Client connected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}