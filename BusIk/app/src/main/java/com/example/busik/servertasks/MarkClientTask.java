package com.example.busik.servertasks;

import android.os.AsyncTask;

import com.example.busik.other.ServerWork;
import com.example.busik.other.Trip;
import com.example.busik.client.Client;
import com.example.busik.client.ClientListAdapter;

import java.io.IOException;

public class MarkClientTask extends AsyncTask<Integer,Void,String> {
    private Client client;
    private ClientListAdapter adapter;
    private Trip trip;
    private int position;

    public MarkClientTask(Client client, ClientListAdapter adapter, Trip trip, int position) {
        this.client = client;
        this.adapter = adapter;
        this.trip = trip;
        this.position = position;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        String request = "MARK--" + trip.getId() + "--" + client.getId() + "--" + ints[0];
        try {
            return ServerWork.sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if(response != null) {
            String[] parts = response.split("--");
            if(!parts[1].equals("DENY")) {
                if(client.getArrived() != 2 && parts[1].equals("2"))
                    trip.changeSeats(1);
                if(client.getArrived() == 2 && parts[1].equals("1"))
                    trip.changeSeats(-1);
                client.setArrived(Integer.parseInt(parts[1]));
                adapter.notifyItemChanged(position);
            }
        }
    }
}
