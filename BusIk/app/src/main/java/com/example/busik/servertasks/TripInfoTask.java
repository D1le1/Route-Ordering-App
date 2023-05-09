package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.ServerWork;
import com.example.busik.Trip;
import com.example.busik.client.Client;
import com.example.busik.client.ClientListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TripInfoTask extends AsyncTask<Integer,Void,String> {

    private Context context;
    private Activity activity;
    private Trip trip;

    public TripInfoTask(Trip trip, Context context) {
        this.trip = trip;
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        String request = "TRIP--" + trip.getId();
        try {
            return ServerWork.sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                List<Client> clients = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    MyJSONObject object = new MyJSONObject(jsonArray.getJSONObject(i));
                    clients.add(object.parseToClient());
                }

                ClientListAdapter mClientListAdapter = new ClientListAdapter(clients, trip);

                // Настройка RecyclerView
                RecyclerView mClientListView = activity.findViewById(R.id.client_list);
                mClientListView.setLayoutManager(new LinearLayoutManager(context));
                mClientListView.setAdapter(mClientListAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
