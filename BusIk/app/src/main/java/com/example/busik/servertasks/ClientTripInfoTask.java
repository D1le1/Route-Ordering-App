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
                JSONArray jsonArray = new JSONArray(response);
                List<Stop> stops = new ArrayList<>();
                for(int i=0; i<jsonArray.length()-1; i++)
                {
                    JSONObject object = jsonArray.getJSONObject(i);
                    stops.add(new Stop(object.getString("name"), object.getInt("number")));
                }

                JSONObject object = jsonArray.getJSONObject(jsonArray.length()-1);
                fillLayout(stops, object.getString("name"), object.getInt("number"));

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

    private void fillLayout(List<Stop> stops, String name, int number)
    {
        TextView driverName = activity.findViewById(R.id.driver_name);
        TextView departureTime = activity.findViewById(R.id.departure_time);
        TextView destinationTime = activity.findViewById(R.id.destination_time);
        TextView departureCity = activity.findViewById(R.id.departure_city);
        TextView destinationCity = activity.findViewById(R.id.destination_city);

        Spinner departureSpinner = activity.findViewById(R.id.departure_spinner);

        departureTime.setText(trip.getStartTime());
        destinationTime.setText(trip.getEndTime());
        departureCity.setText(trip.getRoute().split("-")[0]);
        destinationCity.setText(trip.getRoute().split("-")[1]);

        driverName.setText(name);

        List<String> names = new ArrayList<>();
        for(Stop s : stops)
        {
            names.add(s.getName());
        }
        ArrayAdapter<String> departureAdapter = new ArrayAdapter<>(context, R.layout.spinner_text, names);
        departureSpinner.setAdapter(departureAdapter);
    }
}
