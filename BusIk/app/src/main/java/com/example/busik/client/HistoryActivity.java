package com.example.busik.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.other.Trip;
import com.example.busik.servertasks.TripsTask;

import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        List<Trip> trips = Arrays.asList(
                new Trip(0, "Минск-Бобруйск", "11:00", "13:00", 5, "", ""),
                new Trip(0, "Бобруйск-Минск", "20:00", "22:00", 5, "", ""),
                new Trip(0, "Минск-Гомель", "10:00", "13:30", 5, "", ""),
                new Trip(0, "Минск-Бобруйск", "21:00", "00:30", 5, "", "")
        );

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_trips);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        HistoryListAdapter adapter = new HistoryListAdapter(trips);
        mRecyclerView.setAdapter(adapter);
//        client = (Client) getIntent().getSerializableExtra("client");

//        new TripsTask(this, client).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new TripsTask(this, client).execute();
    }
}