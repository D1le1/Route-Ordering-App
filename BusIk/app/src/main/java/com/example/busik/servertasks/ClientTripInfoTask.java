package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.client.ClientListAdapter;
import com.example.busik.other.ServerWork;
import com.example.busik.other.Stop;
import com.example.busik.other.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientTripInfoTask extends AsyncTask<Integer,Void,String> {

    private Context context;
    private Activity activity;
    private Trip trip;

    public ClientTripInfoTask(Trip trip, Context context) {
        this.trip = trip;
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        String request = "INFO--" + trip.getId();
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
                activity.findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
                JSONObject object = new JSONObject(response);
                List<String> stops = Arrays.asList(object.getString("stops").split(","));

                fillLayout(
                        stops, object.getString("name"),
                        object.getString("number"),
                        object.getString("mark"),
                        object.getString("color"),
                        object.getString("cost")
                );

            } catch (JSONException e) {
                e.printStackTrace();
                activity.findViewById(R.id.error).setVisibility(View.VISIBLE);
            }
        }
        else
        {
            activity.findViewById(R.id.error).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);
        }
    }

    private void fillLayout(List<String> stops, String name, String number, String mark, String color, String cost)
    {
        TextView driverName = activity.findViewById(R.id.driver_name);
        TextView departureTime = activity.findViewById(R.id.departure_time);
        TextView destinationTime = activity.findViewById(R.id.destination_time);
        TextView departureCity = activity.findViewById(R.id.departure_city);
        TextView destinationCity = activity.findViewById(R.id.destination_city);
        TextView busNumber = activity.findViewById(R.id.bus_number);
        TextView busMark = activity.findViewById(R.id.bus_mark);
        TextView busColor = activity.findViewById(R.id.bus_color);
        TextView tripCost = activity.findViewById(R.id.trip_cost);

        Spinner departureSpinner = activity.findViewById(R.id.departure_spinner);

        departureTime.setText(trip.getStartTime());
        destinationTime.setText(trip.getEndTime());
        departureCity.setText(trip.getRoute().split("-")[0]);
        destinationCity.setText(trip.getRoute().split("-")[1]);

        driverName.setText("Водитель: " + name);
        busMark.setText("Марка маршрутки: " + mark);
        busNumber.setText("Номер маршрутки: " + number);
        busColor.setText("Цвет маршрутки: " + color);
        tripCost.setText("Цена: " + cost + " руб.");

        ArrayAdapter<String> departureAdapter = new ArrayAdapter<>(context, R.layout.spinner_text, stops);
        departureSpinner.setAdapter(departureAdapter);
    }
}
