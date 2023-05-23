package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.servertasks.DriversListTask;

import java.util.Arrays;
import java.util.List;

public class DriverListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);

        new DriversListTask(this).execute();

//        fillLayout();
//
//        List<Client> clients = Arrays.asList(
//                new Client("Дроздов Роман", "375445058007", "Белый Mercedes AB 4003-6"),
//                new Client("Ианов Иван", "375297562929", "Красный Volksawagen CC 5120-7"),
//                new Client("Зарипов Антон", "375299071212", "Синий Citroen CI 1010-7"),
//                new Client("Галошко Марина", "375297081920", "Желтый Fiat KP 1122-7")
//        );
//
//        RecyclerView recyclerView = findViewById(R.id.recycler_view_drivers);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        DriverListAdapter driverListAdapter = new DriverListAdapter(clients, v -> {
//            Intent intent = new Intent(this, OperatorManageDriverActivity.class);
//            startActivity(intent);
//        });
//        recyclerView.setAdapter(driverListAdapter);

    }

    public void fillLayout()
    {

    }
}