package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.ServerWork;
import com.example.busik.Trip;
import com.example.busik.client.Client;
import com.example.busik.client.TripListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SerachTripsTask extends AsyncTask<String,Void,String> {

    private Context context;
    private Client client;

    public SerachTripsTask(Context context, Client client) {
        this.context = context;
        this.client = client;
    }

    public SerachTripsTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String request = "SEARCH--" + strings[0] + "--" + strings[1];
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
            try {
                JSONArray jsonArray = new JSONArray(response);
                List<Trip> trips = new ArrayList<>();
                Log.v("D1le", response);

                for(int i=0; i<jsonArray.length(); i++)
                {
                    MyJSONObject object = new MyJSONObject(jsonArray.getJSONObject(i));
                    trips.add(object.parseToTrip());
                }
                TripListAdapter.OnTripClickListener onTripClickListener = (trip) -> {

                };

                RecyclerView mRecyclerView = ((Activity) context).findViewById(R.id.recycler_view_trips);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                TripListAdapter mAdapter = new TripListAdapter(trips, onTripClickListener);
                mRecyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}