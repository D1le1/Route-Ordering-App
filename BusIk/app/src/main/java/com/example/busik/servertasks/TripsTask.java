package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.busik.R;
import com.example.busik.ServerWork;
import com.example.busik.client.ClientActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class TripsTask extends AsyncTask<Void,Void,String> {

    private ServerWork serverWork;
    private Context context;

    public TripsTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String request = "TRIPS--GET";
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
                Log.v("JSONO", jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
