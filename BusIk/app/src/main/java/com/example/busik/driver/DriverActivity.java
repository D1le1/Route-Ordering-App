package com.example.busik.driver;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.ServerWork;
import com.example.busik.Trip;
import com.example.busik.client.Client;
import com.example.busik.client.TripListAdapter;
import com.example.busik.servertasks.TripsTask;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity {

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        client = (Client) getIntent().getSerializableExtra("client");

        new TripsTask(this, client).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new TripsTask(this, client).execute();
    }
}