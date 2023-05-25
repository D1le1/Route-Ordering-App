package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.operator.BusListAdapter;
import com.example.busik.operator.BusManageActivity;
import com.example.busik.operator.DriverListAdapter;
import com.example.busik.operator.OperatorManageDriverActivity;
import com.example.busik.other.ServerWork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusesListTask extends AsyncTask<Integer,Void,String> {
    private Context context;
    private Activity activity;
    private boolean manage;
    private int tripId;

    public BusesListTask(Context context, boolean manage, int tripId) {
        this.context = context;
        activity = (Activity) context;
        this.manage = manage;
        this.tripId = tripId;
    }

    @Override
    protected String doInBackground(Integer... integers) {
        String request = "BUSES--" + (manage ? 1 : 0);
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
                List<JSONObject> objects = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    objects.add(object);
                }
                BusListAdapter.OnBusClickListener onBusClickListener;
                if(manage)
                    onBusClickListener = object -> {
                        Intent intent = new Intent();
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    };
                else
                    onBusClickListener = object -> {
                        Intent intent = new Intent(context, BusManageActivity.class);
                        try {
                            intent.putExtra("id", object.getInt("id"));
                            intent.putExtra("mark", object.getString("mark"));
                            intent.putExtra("color", object.getString("color"));
                            intent.putExtra("number", object.getString("number"));
                            intent.putExtra("name", object.getString("name"));
                            intent.putExtra("driver_id", object.getInt("driver_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        activity.startActivityForResult(intent, 0);
                    };

                RecyclerView recyclerView = activity.findViewById(R.id.recycler_view_buses);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                BusListAdapter busListAdapter = new BusListAdapter(objects, onBusClickListener);
                recyclerView.setAdapter(busListAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
