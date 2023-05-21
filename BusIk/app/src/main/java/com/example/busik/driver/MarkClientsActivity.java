package com.example.busik.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.busik.R;
import com.example.busik.other.Trip;
import com.example.busik.client.ClientListAdapter;
import com.example.busik.servertasks.DriverTripInfoTask;

public class MarkClientsActivity extends AppCompatActivity {

    private RecyclerView mClientListView;
    private ClientListAdapter mClientListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_clients);

        Trip trip = (Trip) getIntent().getSerializableExtra("trip");
        new DriverTripInfoTask(trip, this).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}