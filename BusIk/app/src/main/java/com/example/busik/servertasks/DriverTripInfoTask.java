package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.other.ServerWork;
import com.example.busik.other.Trip;
import com.example.busik.client.Client;
import com.example.busik.client.ClientListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriverTripInfoTask extends AsyncTask<Integer,Void,String> {

    private Context context;
    private Activity activity;
    private Trip trip;

    public DriverTripInfoTask(Trip trip, Context context) {
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
        TextView error = activity.findViewById(R.id.error);
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                List<Client> clients = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    MyJSONObject object = new MyJSONObject(jsonArray.getJSONObject(i));
                    clients.add(object.parseToClient());
                }

                if(clients.size() == 0)
                {
                    error.setText("Нет доступных клиентов");
                    error.setVisibility(View.VISIBLE);
                }

                ClientListAdapter mClientListAdapter = new ClientListAdapter(clients, trip, context);

                // Настройка RecyclerView
                RecyclerView mClientListView = activity.findViewById(R.id.client_list);
                mClientListView.setLayoutManager(new LinearLayoutManager(context));
                mClientListView.setAdapter(mClientListAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            error.setText("Ошибка получения данных с сервера");
            error.setVisibility(View.VISIBLE);
        }
    }
}
