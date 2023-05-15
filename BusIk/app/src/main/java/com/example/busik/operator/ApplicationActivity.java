package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.client.ClientListAdapter;
import com.example.busik.other.Trip;
import com.example.busik.servertasks.DriverTripInfoTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity {

    private RecyclerView mClientListView;
    private ApplicationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_applications);

        List<Client> clientList = new ArrayList<>();
        clientList = Arrays.asList(
                new Client("Дроздов Роман", "null", "375445058007", 0, 2),
                new Client("Ианов Иван", "null", "375297562929", 0, 3),
                new Client("Зарипов Антон", "null", "375299071212", 0, 3),
                new Client("Галошко Марина", "null", "375297081920", 0, 2)
        );

        // Создание адаптера для списка клиентов

        adapter = new ApplicationListAdapter(clientList);

        // Настройка RecyclerView
        mClientListView = findViewById(R.id.client_list);
        mClientListView.setLayoutManager(new LinearLayoutManager(this));
        mClientListView.setAdapter(adapter);
    }
}