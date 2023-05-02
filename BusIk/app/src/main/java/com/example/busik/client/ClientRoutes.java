package com.example.busik.client;

public class ClientRoutes {
    private int clientId;
    private int tripId;

    public ClientRoutes(int clientId, int tripId) {
        this.clientId = clientId;
        this.tripId = tripId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getTripId() {
        return tripId;
    }
}
