package com.example.busik.servertasks;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.client.HistoryInfoActivity;
import com.example.busik.client.HistoryListAdapter;
import com.example.busik.client.TripListAdapter;
import com.example.busik.driver.MarkClientsActivity;
import com.example.busik.operator.OperatorManageTripActivity;
import com.example.busik.operator.OperatorTripListAdapter;
import com.example.busik.other.ServerWork;
import com.example.busik.other.Trip;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteOrderTask extends AsyncTask<Integer,Void,String> {

    private Context context;

    public DeleteOrderTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        String request = "DELETE--ORDER--" + ints[0] + "--" + ints[1];
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
            if(response.equals("DELETE--OK"))
            {
                Intent intent = new Intent();
                ((Activity) context).setResult(RESULT_OK, intent);
                ((Activity) context).finish();
            }
        }
    }
}
