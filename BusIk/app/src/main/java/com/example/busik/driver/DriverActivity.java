package com.example.busik.driver;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.ServerWork;
import com.example.busik.Trip;
import com.example.busik.client.Client;
import com.example.busik.servertasks.TripsTask;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity {

    private List<Trip> mTrips;
    private RecyclerView mRecyclerView;
    private DriverTripListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        mTrips = new ArrayList<>();

        Client client = (Client) getIntent().getSerializableExtra("client");

        new TripsTask(mTrips, this, client).execute();
    }
}