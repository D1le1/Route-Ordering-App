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
import com.example.busik.client.Client;
import com.example.busik.client.ClientActivity;
import com.example.busik.driver.DriverActivity;
import com.example.busik.operator.DriverListAdapter;
import com.example.busik.operator.OperatorActivity;
import com.example.busik.operator.OperatorManageDriverActivity;
import com.example.busik.other.ServerWork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriversListTask extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;

    public DriversListTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String request = "DRIVERS-- ";
        try {
            return ServerWork.sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            if(response != null) {
                JSONArray jsonArray = new JSONArray(response);
                List<Client> drivers = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String bus;
                    if(object.has("mark"))
                        bus = object.getString("color") + " " + object.getString("mark") + " " + object.getString("number");
                    else
                        bus = "Нет закрепленного авто";
                    drivers.add(new Client(
                            object.getString("name"),
                            object.getString("phone"),
                            bus
                    ));

                    RecyclerView recyclerView = activity.findViewById(R.id.recycler_view_drivers);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    DriverListAdapter driverListAdapter = new DriverListAdapter(drivers, driver -> {
                        Intent intent = new Intent(context, OperatorManageDriverActivity.class);
                        intent.putExtra("client", driver);
                        activity.startActivity(intent);
                    });
                    recyclerView.setAdapter(driverListAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
