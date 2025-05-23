package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.BookActivity;
import com.example.busik.other.ServerWork;
import com.example.busik.other.Trip;
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
    private Activity activity;

    public SerachTripsTask(Context context, Client client) {
        this.context = context;
        this.client = client;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String request = "SEARCH--" + strings[0] + "--" + strings[1] + "--" + strings[2];
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
            try {
                JSONArray jsonArray = new JSONArray(response);
                List<Trip> trips = new ArrayList<>();

                for(int i=0; i<jsonArray.length(); i++)
                {
                    MyJSONObject object = new MyJSONObject(jsonArray.getJSONObject(i));
                    trips.add(object.parseToTrip());
                }
                TripListAdapter.OnTripClickListener onTripClickListener = (trip) -> {
                    if(trip.getSeats() > 0) {
                        Intent intent = new Intent(context, BookActivity.class);
                        intent.putExtra("trip", trip);
                        intent.putExtra("client", client);
                        ((Activity) context).startActivityForResult(intent, 0);
                    }
                };

                if(trips.size() == 0)
                {
                    error.setText("По вашему запросу ничего не найдено");
                    error.setVisibility(View.VISIBLE);
                }

                RecyclerView mRecyclerView = ((Activity) context).findViewById(R.id.recycler_view_trips);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                TripListAdapter mAdapter = new TripListAdapter(trips, onTripClickListener);
                mRecyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            error.setText("Ошибка получения данных с сервера");
            error.setVisibility(View.VISIBLE);
        }
    }
}
