package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.operator.DriverListAdapter;
import com.example.busik.operator.OperatorManageDriverActivity;
import com.example.busik.operator.OperatorManageTripActivity;
import com.example.busik.other.ServerWork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangeTripDriverTask extends AsyncTask<Integer,Void,String> {
    private Context context;
    private Activity activity;
    private Client driver;

    public ChangeTripDriverTask(Context context, Client driver) {
        this.context = context;
        activity = (Activity) context;
        this.driver = driver;
    }

    @Override
    protected String doInBackground(Integer... integers) {
        String request = "CHANGEDR--" + integers[0] + "--" + integers[1];
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
            if(response.equals("CHANGEDR--OK")) {
                Intent intent = new Intent(context, OperatorManageTripActivity.class);
                intent.putExtra("driver", driver);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        }
    }
}
