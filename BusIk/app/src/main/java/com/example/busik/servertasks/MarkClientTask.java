package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.busik.R;
import com.example.busik.ServerWork;
import com.example.busik.client.Client;
import com.example.busik.client.ClientActivity;
import com.example.busik.client.ClientListAdapter;
import com.example.busik.driver.DriverActivity;

import org.json.JSONException;

import java.io.IOException;

public class MarkClientTask extends AsyncTask<Integer,Void,String> {
    private Client client;
    private ClientListAdapter adapter;
    private int position;

    public MarkClientTask(Client client, ClientListAdapter adapter, int position) {
        this.client = client;
        this.adapter = adapter;
        this.position = position;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        String request = "MARK--" + ints[0] + "--" + ints[1] + "--" + ints[2];
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
            Log.v("D1le", response);
            if(!parts[1].equals("DENY")) {
                    client.setArrived(Integer.parseInt(parts[1]));
                    adapter.notifyItemChanged(position);
            }
        }
    }
}
