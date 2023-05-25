package com.example.busik.servertasks;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.busik.R;
import com.example.busik.other.ServerWork;

import java.io.IOException;

public class DeleteTripTask extends AsyncTask<Integer,Void,String> {

    private Context context;
    private Activity activity;

    public DeleteTripTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        String request = "DELETE--TRIP--" + ints[0];
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
            if(response.equals("DELETE--OK"))
            {
                Intent intent = new Intent();
                ((Activity) context).setResult(RESULT_OK, intent);
                ((Activity) context).finish();
            }
        }
        else
        {
            error.setText("Ошибка отправки данных на сервера");
            error.setVisibility(View.VISIBLE);
        }
    }
}
