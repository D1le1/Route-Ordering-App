package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.busik.R;
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
    private Context context;
    private Activity activity;

    public MarkClientTask(Client client, ClientListAdapter adapter, Trip trip, int position, Context context) {
        this.client = client;
        this.adapter = adapter;
        this.trip = trip;
        this.position = position;
        this.context = context;
        activity = (Activity)context;
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
        TextView error = activity.findViewById(R.id.error);
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
        }else{
            error.setText("Ошибка получения данных с сервера");
            error.setVisibility(View.VISIBLE);
        }
    }
}
