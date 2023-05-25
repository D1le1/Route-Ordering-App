package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.busik.R;
import com.example.busik.other.ServerWork;

import java.io.IOException;

public class UpdateBusTask extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;

    public UpdateBusTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String request = "UPDATE--BUS--" + strings[0] + "--" + strings[1] + "--" + strings[2] + "--" + strings[3] + "--" + strings[4];
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
        }else {
            error.setText("Ошибка отправки данных на сервера");
            error.setVisibility(View.VISIBLE);
        }
    }
}
