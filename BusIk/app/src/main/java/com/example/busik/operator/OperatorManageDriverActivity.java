package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.client.Client;

public class OperatorManageDriverActivity extends AppCompatActivity {

    TextView driverName;
    TextView phoneNumber;
    TextView busNumber;
    TextView busMark;
    TextView busColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_driver);

        Client driver = (Client) getIntent().getSerializableExtra("client");

        fillLayout(driver);

    }

    public void fillLayout(Client driver)
    {
        driverName = findViewById(R.id.driver_name);
        phoneNumber = findViewById(R.id.phone_number);
        busNumber = findViewById(R.id.bus_number);
        busMark = findViewById(R.id.bus_mark);
        busColor = findViewById(R.id.bus_color);

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

        busNumber.setOnClickListener(v -> {
            Intent intent = new Intent(this, BusListActivity.class);
            intent.putExtra("manage", 2);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            int busId = getIntent().getIntExtra("id", 0);
            String markText = data.getStringExtra("mark");
            String colorText = data.getStringExtra("color");
            String numberText = data.getStringExtra("number");

            busColor.setText("Цвет маршрутки: " + colorText);
            busMark.setText("Марка маршрутки: " + markText);
            busNumber.setText("Номер маршрутки: " + numberText);
        }
    }
}