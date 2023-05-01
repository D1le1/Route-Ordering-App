package com.example.busik.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.client.ClientListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MarkClientsActivity extends AppCompatActivity {

    private RecyclerView mClientListView;
    private ClientListAdapter mClientListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_clients);
        // Создание списка клиентов
        List<Client> clientList = new ArrayList<>();
        clientList.add(new Client("Иван Иванов", "ул. Пушкина, д. 10, кв. 25", "+7 999 123-45-67"));
        clientList.add(new Client("Петр Петров", "ул. Ленина, д. 5, кв. 15", "+7 999 234-56-78"));
        clientList.add(new Client("Сергей Сергеев", "ул. Гагарина, д. 20, кв. 10", "+7 999 345-67-89"));
        clientList.add(new Client("Сергей Сергеев", "ул. Гагарина, д. 20, кв. 10", "+7 999 345-67-89"));
        clientList.add(new Client("Сергей Сергеев", "ул. Гагарина, д. 20, кв. 10", "+7 999 345-67-89"));
        clientList.add(new Client("Сергей Сергеев", "ул. Гагарина, д. 20, кв. 10", "+7 999 345-67-89"));
        clientList.add(new Client("Сергей Сергеев", "ул. Гагарина, д. 20, кв. 10", "+7 999 345-67-89"));
        clientList.add(new Client("Сергей Сергеев", "ул. Гагарина, д. 20, кв. 10", "+7 999 345-67-89"));
        clientList.add(new Client("Сергей Сергеев", "ул. Гагарина, д. 20, кв. 10", "+7 999 345-67-89"));
        clientList.add(new Client("Сергей Сергеев", "ул. Гагарина, д. 20, кв. 10", "+7 999 345-67-89"));
        clientList.add(new Client("Сергей Сергеев", "ул. Гагарина, д. 20, кв. 10", "+7 999 345-67-89"));

        // Создание адаптера для списка клиентов
        mClientListAdapter = new ClientListAdapter(clientList);

        // Настройка RecyclerView
        mClientListView = findViewById(R.id.client_list);
        mClientListView.setLayoutManager(new LinearLayoutManager(this));
        mClientListView.setAdapter(mClientListAdapter);
    }
}