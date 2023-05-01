package com.example.busik.driver;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.Trip;

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

        // Заполнение списка рейсов данными
        mTrips = new ArrayList<>();
        mTrips.add(new Trip("Москва - Санкт-Петербург", "10:00"));
        mTrips.add(new Trip("Санкт-Петербург - Москва", "12:00"));
        mTrips.add(new Trip("Москва - Новосибирск", "14:00"));
        mTrips.add(new Trip("Новосибирск - Москва", "16:00"));
        mTrips.add(new Trip("Санкт-Петербург - Новосибирск", "18:00"));
        mTrips.add(new Trip("Новосибирск - Санкт-Петербург", "20:00"));

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