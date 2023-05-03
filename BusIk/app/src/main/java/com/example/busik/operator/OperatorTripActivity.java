package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.driver.DriverTripListAdapter;
import com.example.busik.driver.MarkClientsActivity;
import com.example.busik.R;
import com.example.busik.Trip;

import java.util.ArrayList;
import java.util.List;

public class OperatorTripActivity extends AppCompatActivity {

    private List<Trip> mTrips;
    private RecyclerView mRecyclerView;
    private DriverTripListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        // Заполнение списка рейсов данными

        DriverTripListAdapter.OnTripClickListener onTripClickListener = (mTrips) -> {
            Intent intent = new Intent(this, MarkClientsActivity.class);
            startActivity(intent);
        };

        // Настройка RecyclerView и адаптера
        mRecyclerView = findViewById(R.id.recycler_view_trips);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DriverTripListAdapter(mTrips, onTripClickListener);
        mRecyclerView.setAdapter(mAdapter);
    }
}