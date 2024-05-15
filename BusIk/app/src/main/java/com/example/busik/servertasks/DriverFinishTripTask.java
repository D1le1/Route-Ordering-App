package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.client.ClientListAdapter;
import com.example.busik.other.ServerWork;
import com.example.busik.other.Trip;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriverFinishTripTask extends AsyncTask<Integer,Void,String> {

    private Context context;
    private Activity activity;
    private Trip trip;

    public DriverFinishTripTask(Trip trip, Context context) {
        this.trip = trip;
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        String request = "FINISH--" + ints[0] + "--" + trip.getId();
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
            activity.finish();
        }else{
            error.setText("Ошибка получения данных с сервера");
            error.setVisibility(View.VISIBLE);
        }
    }
}
