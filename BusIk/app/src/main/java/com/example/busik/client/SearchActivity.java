package com.example.busik.client;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.servertasks.SerachTripsTask;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        String start = getIntent().getStringExtra("start");
        String end = getIntent().getStringExtra("end");
        String date = getIntent().getStringExtra("date");

        Client client = (Client) getIntent().getSerializableExtra("client");
        new SerachTripsTask(this, client).execute(start, end, date);
    }
}