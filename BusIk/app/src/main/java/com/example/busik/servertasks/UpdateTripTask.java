package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.client.ClientActivity;
import com.example.busik.driver.DriverActivity;
import com.example.busik.operator.OperatorActivity;
import com.example.busik.other.ServerWork;

import org.json.JSONException;

import java.io.IOException;

public class UpdateTripTask extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;

    public UpdateTripTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String request = "UPDATE--TRIP--" + strings[0] + "--" + strings[1] + "--" + strings[2] + "--" + strings[3] + "--" + strings[4];
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
        if(response != null) {
            if(response.equals("UPDATE--OK"))
            {
                activity.finish();
            }
        }
    }
}
