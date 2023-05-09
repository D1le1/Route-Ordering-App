package com.example.busik.client;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.Trip;
import com.example.busik.client.Client;
import com.example.busik.driver.DriverTripListAdapter;
import com.example.busik.servertasks.SerachTripsTask;
import com.example.busik.servertasks.TripsTask;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Trip> mTrips;
    private RecyclerView mRecyclerView;
    private DriverTripListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String start = getIntent().getStringExtra("start");
        String end = getIntent().getStringExtra("end");
        new SerachTripsTask(this).execute(start, end);
    }
}