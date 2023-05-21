package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.operator.OperatorManageTripActivity;
import com.example.busik.operator.OperatorTripListAdapter;
import com.example.busik.other.ServerWork;
import com.example.busik.other.Trip;
import com.example.busik.client.Client;
import com.example.busik.client.TripListAdapter;
import com.example.busik.driver.MarkClientsActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TripsTask extends AsyncTask<Void,Void,String> {

    private Context context;
    private Client client;

    public TripsTask(Context context, Client client) {
        this.context = context;
        this.client = client;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String request = "TRIPS--" + client.getId() + "--" + client.getRole();
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

                for(int i=0; i<jsonArray.length(); i++)
                {
                    MyJSONObject object = new MyJSONObject(jsonArray.getJSONObject(i));
                    trips.add(object.parseToTrip());
                }

                if(trips.size() == 0)
                {
                    ((Activity) context).findViewById(R.id.error).setVisibility(View.VISIBLE);
                }

                RecyclerView mRecyclerView = ((Activity) context).findViewById(R.id.recycler_view_trips);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                if(client.getRole() == 2)
                {
                    TripListAdapter.OnTripClickListener onTripClickListener = (trip) -> {
                        Intent intent = new Intent(context, MarkClientsActivity.class);
                        intent.putExtra("trip", trip);
                        ((Activity) context).startActivityForResult(intent, 0);
                    };
                    TripListAdapter adapter = new TripListAdapter(trips, onTripClickListener);
                    mRecyclerView.setAdapter(adapter);
                }else if(client.getRole() == 3)
                {
                    OperatorTripListAdapter.OnTripClickListener onTripClickListener = (trip) -> {
                        Intent intent = new Intent(context, OperatorManageTripActivity.class);
                        intent.putExtra("trip", trip);
                        ((Activity) context).startActivityForResult(intent, 0);
                    };
                    OperatorTripListAdapter adapter = new OperatorTripListAdapter(trips, onTripClickListener);
                    mRecyclerView.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
