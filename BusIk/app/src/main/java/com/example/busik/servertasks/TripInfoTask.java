//package com.example.busik.servertasks;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.busik.R;
//import com.example.busik.ServerWork;
//import com.example.busik.Trip;
//import com.example.busik.driver.DriverTripListAdapter;
//import com.example.busik.driver.MarkClientsActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.List;
//
//public class TripInfoTask extends AsyncTask<Void,Void,String> {
//
//    private Context context;
//    private int tripId;
//
//    public TripInfoTask(int tripId, Context context) {
//        this.tripId = tripId;
//        this.context = context;
//    }
//
//    @Override
//    protected String doInBackground(Void... voids) {
//        String request = "TRIP--" + voids[0];
//        try {
//            return ServerWork.sendRequest(request);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String response) {
//        if(response != null) {
//            try {
//                JSONArray jsonArray = new JSONArray(response);
//
//                for(int i=0; i<jsonArray.length(); i++)
//                {
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    Trip trip = new Trip(
//                            object.getString("route"), object.getString("time")
//                    );
//                    mTrips.add(trip);
//                }
//                DriverTripListAdapter.OnTripClickListener onTripClickListener = (mTrips) -> {
//                      Intent intent = new Intent(context, MarkClientsActivity.class);
//                      context.startActivity(intent);
//                };
//
//                RecyclerView mRecyclerView = ((Activity) context).findViewById(R.id.recycler_view_trips);
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//                DriverTripListAdapter mAdapter = new DriverTripListAdapter(mTrips, onTripClickListener);
//                mRecyclerView.setAdapter(mAdapter);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
