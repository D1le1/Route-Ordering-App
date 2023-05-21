package com.example.busik.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.servertasks.SerachTripsTask;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        fillTrips();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        fillTrips();
    }

    private void fillTrips()
    {
        String start = getIntent().getStringExtra("start");
        String end = getIntent().getStringExtra("end");
        String date = getIntent().getStringExtra("date");

        Client client = (Client) getIntent().getSerializableExtra("client");
        new SerachTripsTask(this, client).execute(start, end, date);
    }
}