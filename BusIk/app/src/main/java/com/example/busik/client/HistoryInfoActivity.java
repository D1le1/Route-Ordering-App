package com.example.busik.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.other.Trip;
import com.example.busik.servertasks.ClientTripInfoTask;
import com.example.busik.servertasks.HistoryInfoTask;

public class HistoryInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_info);

        Trip trip = (Trip) getIntent().getSerializableExtra("trip");
        Client client = (Client) getIntent().getSerializableExtra("client");
        new HistoryInfoTask(client, trip, this).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}