package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.busik.R;
import com.example.busik.other.ServerWork;

import java.io.IOException;

public class ConfirmOrderTask extends AsyncTask<String,Void,String> {
    private Context context;
    private Activity activity;

    public ConfirmOrderTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String request = "CONFIRM--" + strings[0] + "--" + strings[1] + "--" + strings[2];
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
            if(response.equals("CONFIRM--OK"))
            {
                activity.finish();
            }
            else{
                TextView error = activity.findViewById(R.id.mini_error);
                error.setVisibility(View.VISIBLE);
            }
        }
    }
}
