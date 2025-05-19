package com.example.busik.servertasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.busik.client.Client;
import com.example.busik.client.ClientListAdapter;
import com.example.busik.operator.ApplicationListAdapter;
import com.example.busik.other.ServerWork;

import java.io.IOException;
import java.util.List;

public class ApplicationTask extends AsyncTask<Integer,Void,String> {
    private Client client;
    private List<Client> clients;
    private ApplicationListAdapter adapter;
    private int position;

    private Context context;

    public ApplicationTask(Client client, ApplicationListAdapter adapter, List<Client> clients, int position) {
        this.client = client;
        this.adapter = adapter;
        this.clients = clients;
        this.position = position;
        this.context = context;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        String request = "APPLICATION--" + client.getId() + "--" + ints[0];
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
            if(response.equals("APPLICATION--OK"))
            {
                clients.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, clients.size());
            }

//            String[] parts = response.split("--");
//            if(!parts[1].equals("DENY")) {
//                if(client.getArrived() != 2 && parts[1].equals("2"))
//                    trip.changeSeats(1);
//                if(client.getArrived() == 2 && parts[1].equals("1"))
//                    trip.changeSeats(-1);
//                client.setArrived(Integer.parseInt(parts[1]));
//                adapter.notifyItemChanged(position);
        }
    }
}
