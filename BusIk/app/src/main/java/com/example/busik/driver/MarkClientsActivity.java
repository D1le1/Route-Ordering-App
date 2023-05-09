package com.example.busik.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.busik.R;
import com.example.busik.Trip;
import com.example.busik.client.Client;
import com.example.busik.client.ClientListAdapter;
import com.example.busik.client.TripListAdapter;
import com.example.busik.servertasks.TripInfoTask;

import java.util.ArrayList;
import java.util.List;

public class MarkClientsActivity extends AppCompatActivity {

    private RecyclerView mClientListView;
    private ClientListAdapter mClientListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_clients);

        Trip trip = (Trip) getIntent().getSerializableExtra("trip");
        new TripInfoTask(trip, this).execute();
        // Создание списка клиентов
//        List<Client> clientList = new ArrayList<>();
//
//        // Создание адаптера для списка клиентов
//        mClientListAdapter = new ClientListAdapter(clientList);
//
//        // Настройка RecyclerView
//        mClientListView = findViewById(R.id.client_list);
//        mClientListView.setLayoutManager(new LinearLayoutManager(this));
//        mClientListView.setAdapter(mClientListAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}