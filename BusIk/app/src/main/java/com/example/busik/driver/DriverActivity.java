package com.example.busik.driver;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.servertasks.TripsTask;

public class DriverActivity extends AppCompatActivity {

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        client = (Client) getIntent().getSerializableExtra("client");

        new TripsTask(this, client).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new TripsTask(this, client).execute();
    }
}