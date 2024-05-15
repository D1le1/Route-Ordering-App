package com.example.busik.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.busik.R;
import com.example.busik.other.Trip;
import com.example.busik.client.ClientListAdapter;
import com.example.busik.servertasks.DriverFinishTripTask;
import com.example.busik.servertasks.DriverTripInfoTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MarkClientsActivity extends AppCompatActivity {

    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_clients);

        button = findViewById(R.id.finishButt);

        Trip trip = (Trip) getIntent().getSerializableExtra("trip");
        new DriverTripInfoTask(trip, this).execute();

        button.setOnClickListener(v ->{
            new DriverFinishTripTask(trip, this).execute(1);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}