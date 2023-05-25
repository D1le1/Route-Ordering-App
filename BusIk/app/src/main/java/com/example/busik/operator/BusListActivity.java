package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.servertasks.BusesListTask;

import java.util.Arrays;
import java.util.List;

public class BusListActivity extends AppCompatActivity {

    private Button manageTrips;
    private Button manageDrivers;
    private Button manageApplications;
    private Button manageBuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        int manage = getIntent().getIntExtra("manage", 0);
        int tripId = getIntent().getIntExtra("trip_id", 0);

        new BusesListTask(this, manage, tripId).execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int manage = getIntent().getIntExtra("manage", 2);
        int tripId = getIntent().getIntExtra("trip_id", 0);

        new BusesListTask(this, manage, tripId).execute();
    }
}