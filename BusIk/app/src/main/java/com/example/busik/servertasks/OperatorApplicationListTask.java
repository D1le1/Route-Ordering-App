package com.example.busik.servertasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.client.ClientListAdapter;
import com.example.busik.operator.ApplicationListAdapter;
import com.example.busik.other.ServerWork;
import com.example.busik.other.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperatorApplicationListTask extends AsyncTask<Integer,Void,String> {

    private Context context;
    private Activity activity;

    public OperatorApplicationListTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected String doInBackground(Integer... ints) {
        String request = "APPLICATION--LIST";
        try {
            return ServerWork.sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                List<Client> clients = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    clients.add(new Client(
                            object.getString("name"),
                            "",
                            object.getString("number"),
                            object.getInt("id"),
                            object.getInt("role")
                    ));
                }

                if(clients.size() == 0)
                {
                    ((Activity) context).findViewById(R.id.error).setVisibility(View.VISIBLE);
                }

                ApplicationListAdapter applicationListAdapter = new ApplicationListAdapter(clients);

                RecyclerView mClientListView = activity.findViewById(R.id.client_list);
                mClientListView.setLayoutManager(new LinearLayoutManager(context));
                mClientListView.setAdapter(applicationListAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
