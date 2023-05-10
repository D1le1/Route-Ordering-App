package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.busik.R;
import com.example.busik.ServerWork;
import com.example.busik.client.Client;
import com.example.busik.client.ClientActivity;
import com.example.busik.driver.DriverActivity;

import org.json.JSONException;

import java.io.IOException;

public class RegisterTask extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;

    public RegisterTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String request = "REG--" + strings[0] + "--" + strings[1] + "--" + strings[2] + "--" + strings[3];
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
            if(response.equals("REG--OK"))
            {
                error.setVisibility(View.INVISIBLE);
                activity.finish();
            }else {
                error.setText("Не все поля заполнены");
                error.setVisibility(View.VISIBLE);
            }
        }else {
            error.setText("Нет соединения с сервером");
            error.setVisibility(View.VISIBLE);
        }
    }
}
