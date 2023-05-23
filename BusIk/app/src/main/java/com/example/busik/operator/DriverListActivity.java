package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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

        boolean manage = getIntent().getBooleanExtra("manage", false);
        int tripId = getIntent().getIntExtra("trip_id", 0);

        new DriversListTask(this, manage, tripId).execute();

    }
}