package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.busik.AuthActivity;
import com.example.busik.R;
import com.example.busik.ServerWork;
import com.example.busik.client.Client;
import com.example.busik.client.ClientActivity;
import com.example.busik.driver.DriverActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AuthTask extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;

    public AuthTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String request = "AUTH--" + strings[0];
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
                MyJSONObject object = new MyJSONObject(response);
                Client client = object.parseToClient();
                Intent intent;
                switch (client.getRole()) {
                    case 1:
                        intent = new Intent(context, ClientActivity.class);
                        intent.putExtra("client", client);
                        context.startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(context, DriverActivity.class);
                        intent.putExtra("client", client);
                        context.startActivity(intent);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                TextView title = activity.findViewById(R.id.title);
                title.setText("Неправильный логин");
            }
        }
    }
}
