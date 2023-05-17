package com.example.busik.client;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.other.Trip;
import com.example.busik.servertasks.ClientTripInfoTask;

import java.util.Arrays;
import java.util.List;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Trip trip = (Trip) getIntent().getSerializableExtra("trip");
        Client client = (Client) getIntent().getSerializableExtra("client");
        new ClientTripInfoTask(client, trip, this).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}