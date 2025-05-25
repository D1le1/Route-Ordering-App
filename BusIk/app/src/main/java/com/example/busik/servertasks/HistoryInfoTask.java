package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.other.CryptoUtils;
import com.example.busik.other.ServerWork;
import com.example.busik.other.Trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HistoryInfoTask extends AsyncTask<Integer,Void,String> {

    private Context context;
    private Activity activity;
    private Client client;
    private Trip trip;
    private int arrived;

    public HistoryInfoTask(Client client, Trip trip, Context context) {
        this.client = client;
        this.trip = trip;
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        String request = "HISTORY--" + client.getId() + "--" + trip.getId();
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
                arrived = object.getInt("arrived");
                fillLayout(
                        object.getString("stop"),
                        object.getString("name"),
                        object.getString("phone"),
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fillLayout(String stop, String name, String phone, String number, String mark, String color, String cost)
    {
        TextView driverName = activity.findViewById(R.id.driver_name);
        TextView driverPhone = activity.findViewById(R.id.phone_number);
        TextView departureTime = activity.findViewById(R.id.departure_time);
        TextView destinationTime = activity.findViewById(R.id.destination_time);
        TextView departureCity = activity.findViewById(R.id.departure_city);
        TextView destinationCity = activity.findViewById(R.id.destination_city);
        TextView busNumber = activity.findViewById(R.id.bus_number);
        TextView busMark = activity.findViewById(R.id.bus_mark);
        TextView busColor = activity.findViewById(R.id.bus_color);
        TextView tripCost = activity.findViewById(R.id.trip_cost);
        TextView stopName = activity.findViewById(R.id.stop_name);

        Button declineOrder = activity.findViewById(R.id.btn_decline_order);

        departureTime.setText(trip.getStartTime());
        destinationTime.setText(trip.getEndTime());
        departureCity.setText(trip.getRoute().split("-")[0]);
        destinationCity.setText(trip.getRoute().split("-")[1]);

        stopName.setText("Остановка: " + stop);
        driverName.setText("Водитель: " + name);
        driverPhone.setText("Номер телефона: " + CryptoUtils.decrypt(phone));
        busMark.setText("Марка маршрутки: " + mark);
        busNumber.setText("Номер маршрутки: " + number);
        busColor.setText("Цвет маршрутки: " + color);
        tripCost.setText("Цена: " + cost + " руб.");

        if(trip.getFinished() == 0 && arrived == 0)
        {
            declineOrder.setVisibility(View.VISIBLE);
            declineOrder.setOnClickListener(v -> new DeleteOrderTask(context).execute(client.getId(), trip.getId()));
        }
    }
}
