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
import com.example.busik.servertasks.DriversListTask;

import java.util.Arrays;
import java.util.List;

public class DriverListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);

        int manage = getIntent().getIntExtra("manage", 0);
        int tripId = getIntent().getIntExtra("trip_id", 0);
        int busId = getIntent().getIntExtra("bus_id", 0);

        new DriversListTask(this, manage, tripId, busId).execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int manage = getIntent().getIntExtra("manage", 0);
        int tripId = getIntent().getIntExtra("trip_id", 0);
        int busId = getIntent().getIntExtra("bus_id", 0);

        new DriversListTask(this, manage, tripId, busId).execute();
    }
}