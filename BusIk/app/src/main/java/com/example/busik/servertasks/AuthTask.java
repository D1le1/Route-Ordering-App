package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.busik.AuthActivity;
import com.example.busik.R;
import com.example.busik.ServerWork;
import com.example.busik.client.ClientActivity;

import java.io.IOException;

public class AuthTask extends AsyncTask<Void,Void,String> {
    private String username;
    private ServerWork serverWork;
    private Context context;

    public AuthTask(String username, ServerWork serverWork, Context context) {
        this.username = username;
        this.serverWork = serverWork;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String request = "AUTH--" + username;
        try {
            return serverWork.sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if(response != null) {
            if (response.equals("AUTH--OK")) {
                Intent intent = new Intent(context, ClientActivity.class);
                context.startActivity(intent);
            } else {
                TextView title = ((Activity) context).findViewById(R.id.title);
                title.setText("Неправильный логин");
            }
        }
    }
}
