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
    private int manage;
    private int tripId;

    private TextView error;

    public BusesListTask(Context context, int manage, int tripId) {
        this.context = context;
        activity = (Activity) context;
        this.manage = manage;
        this.tripId = tripId;
    }

    @Override
    protected String doInBackground(Integer... integers) {
        String request = "BUSES--" + manage;
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
        try {
            if(response != null) {
                JSONArray jsonArray = new JSONArray(response);
                List<JSONObject> objects = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    objects.add(object);
                }

                if(objects.size() == 0)
                {
                    error.setText("Нет свободных маршрутных такси");
                    error.setVisibility(View.VISIBLE);
                }

                BusListAdapter.OnBusClickListener onBusClickListener;
                if(manage == 1 || manage == 2)
                    onBusClickListener = object -> {
                        Intent intent = new Intent();
                        try {
                            intent.putExtra("id", object.getInt("id"));
                            intent.putExtra("mark", object.getString("mark"));
                            intent.putExtra("number", object.getString("number"));
                            intent.putExtra("color", object.getString("color"));

                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
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
            else {
                error.setText("Ошибка получения данных с сервера");
                error.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
