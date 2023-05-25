package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.busik.R;
import com.example.busik.operator.OperatorActivity;
import com.example.busik.other.ServerWork;

import com.example.busik.client.Client;
import com.example.busik.client.ClientActivity;
import com.example.busik.driver.DriverActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.OpenOption;

public class AuthTask extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;

    public AuthTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String request = "AUTH--" + strings[0] + "--" + strings[1];
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
            try {
                MyJSONObject object = new MyJSONObject(response);
                Client client = object.parseToClient();
                int apply = object.getInt("apply");
                Intent intent = null;
                switch (client.getRole()) {
                    case 1:
                        intent = new Intent(context, ClientActivity.class);
                        break;
                    case 2:
                        intent = new Intent(context, DriverActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context, OperatorActivity.class);
                        break;
                }
                if(apply == 1) {
                    intent.putExtra("client", client);
                    context.startActivity(intent);
                    error.setVisibility(View.INVISIBLE);
                }
                else {
                    error.setText("Ваш аккаунт еще не одобрен оператором");
                    error.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();

                error.setText("Неправильный логин или пароль");
                error.setVisibility(View.VISIBLE);
            }
        }else {
            error.setText("Нет соединения с сервером");
            error.setVisibility(View.VISIBLE);
        }
    }
}
