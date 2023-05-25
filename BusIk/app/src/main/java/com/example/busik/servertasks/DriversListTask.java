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
import java.util.Arrays;
import java.util.List;

public class DriversListTask extends AsyncTask<Integer,Void,String> {
    private Context context;
    private Activity activity;
    private int manage;
    private int tripId;
    private int busId;

    public DriversListTask(Context context, int manage, int tripId, int busId) {
        this.context = context;
        activity = (Activity) context;
        this.manage = manage;
        this.tripId = tripId;
        this.busId = busId;
    }

    @Override
    protected String doInBackground(Integer... integers) {
        String request = "DRIVERS--" + manage;
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
                    List<String> bus = new ArrayList<>();
                    if (object.has("mark"))
                        bus = Arrays.asList(object.getString("color"), object.getString("mark"), object.getString("number"));
                    else
                        bus.add("Нет закрепленного авто");
                    drivers.add(new Client(
                            object.getInt("id"),
                            object.getString("name"),
                            object.getString("phone"),
                            bus
                    ));
                }
                DriverListAdapter.OnDriverClickListener onDriverClickListener;
                if(manage == 1 || manage == 2)
                    onDriverClickListener = driver -> {
                        Intent intent = new Intent();
                        intent.putExtra("driver", driver);
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    };
                else
                    onDriverClickListener = driver -> {
                        Intent intent = new Intent(context, OperatorManageDriverActivity.class);
                        intent.putExtra("client", driver);
                        activity.startActivity(intent);
                    };

                RecyclerView recyclerView = activity.findViewById(R.id.recycler_view_drivers);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                DriverListAdapter driverListAdapter = new DriverListAdapter(drivers, onDriverClickListener);
                recyclerView.setAdapter(driverListAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
