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
import com.example.busik.client.HistoryInfoActivity;
import com.example.busik.client.HistoryListAdapter;
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
    private Activity activity;

    private TextView error;

    public TripsTask(Context context, Client client) {
        this.context = context;
        this.client = client;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String request = "TRIPS--" + client.getId() + "--" + client.getRole();
        error = activity.findViewById(R.id.error);
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
                List<Integer> arrives = new ArrayList<>();
                List<String> driversNames = new ArrayList<>();
                List<Integer> driversIds = new ArrayList<>();

                for(int i=0; i<jsonArray.length(); i++)
                {
                    MyJSONObject object = new MyJSONObject(jsonArray.getJSONObject(i));
                    trips.add(object.parseToTrip());
                    if(object.has("arrived"))
                        arrives.add(object.getInt("arrived"));
                    if(object.has("driver_name")) {
                        driversNames.add(object.getString("driver_name"));
                    }
                    if(object.has("driver_id"))
                    {
                        driversIds.add(object.getInt("driver_id"));
                    }
                    else
                    {
                        driversNames.add("Нет закрепленного водителя");
                    }
                }

                if(trips.size() == 0)
                {
                    error.setText("Нет ни одного рейса");
                    error.setVisibility(View.VISIBLE);
                }

                RecyclerView mRecyclerView = ((Activity) context).findViewById(R.id.recycler_view_trips);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                switch (client.getRole())
                {
                    case 1:
                    {
                        HistoryListAdapter.OnTripClickListener onTripClickListener = (trip) -> {
                            Intent intent = new Intent(context, HistoryInfoActivity.class);
                            intent.putExtra("trip", trip);
                            intent.putExtra("client", client);
                            ((Activity) context).startActivityForResult(intent, 0);
                        };
                        HistoryListAdapter adapter = new HistoryListAdapter(trips, arrives, onTripClickListener);
                        mRecyclerView.setAdapter(adapter);
                        break;
                    }
                    case 2: {
                        TripListAdapter.OnTripClickListener onTripClickListener = (trip) -> {
                            Intent intent = new Intent(context, MarkClientsActivity.class);
                            intent.putExtra("trip", trip);
                            ((Activity) context).startActivityForResult(intent, 0);
                        };
                        TripListAdapter adapter = new TripListAdapter(trips, onTripClickListener);
                        mRecyclerView.setAdapter(adapter);
                        break;
                    }
                    case 3: {
                        OperatorTripListAdapter.OnTripClickListener onTripClickListener = (trip, pos) -> {
                            Intent intent = new Intent(context, OperatorManageTripActivity.class);
                            intent.putExtra("trip", trip);
                            intent.putExtra("driver_name", (driversNames.get(pos)));
                            intent.putExtra("driver_id", driversIds.get(pos));
                            ((Activity) context).startActivityForResult(intent, 0);
                        };
                        OperatorTripListAdapter adapter = new OperatorTripListAdapter(trips, onTripClickListener);
                        mRecyclerView.setAdapter(adapter);
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            error.setText("Ошибка получения данных с сервера");
            error.setVisibility(View.VISIBLE);
        }
    }
}
