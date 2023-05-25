package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.client.Client;

public class OperatorManageDriverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_driver);

        Client driver = (Client) getIntent().getSerializableExtra("client");

        fillLayout(driver);

    }

    public void fillLayout(Client driver)
    {
        TextView driverName = findViewById(R.id.driver_name);
        TextView phoneNumber = findViewById(R.id.phone_number);
        TextView busNumber = findViewById(R.id.bus_number);
        TextView busMark = findViewById(R.id.bus_mark);
        TextView busColor = findViewById(R.id.bus_color);

        driverName.setText(driver.getName());
        phoneNumber.setText(driver.getPhone());
        if(!driver.getBus().get(0).equals("Нет закрепленного авто")) {
            busColor.setText("Цвет маршрутки: " + driver.getBus().get(0));
            busMark.setText("Марка маршрутки: " + driver.getBus().get(1));
            busNumber.setText("Номер маршрутки: " + driver.getBus().get(2));
        }else
        {
            busNumber.setText("Нет закрепленного авто");
            busMark.setText("Марка маршрутки: Отсутствует");
            busColor.setText("Цвет маршрутки: Отсутствует");
        }
    }
}