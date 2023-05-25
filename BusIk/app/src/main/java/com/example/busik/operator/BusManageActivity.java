package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.servertasks.UpdateBusTask;

import org.json.JSONException;
import org.json.JSONObject;

public class BusManageActivity extends AppCompatActivity {

    private TextView mark;
    private TextView name;
    private TextView color;
    private TextView number;

    private Button confirmEdits;

    private int driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);
        mark = findViewById(R.id.bus_mark);
        name = findViewById(R.id.driver_name);
        color = findViewById(R.id.bus_color);
        number = findViewById(R.id.bus_number);
        confirmEdits = findViewById(R.id.btn_confirm_edits);

        int busId = getIntent().getIntExtra("id", 0);
        driverId = getIntent().getIntExtra("driver_id", 0);
        String markText = getIntent().getStringExtra("mark");
        String nameText = getIntent().getStringExtra("name");
        String colorText = getIntent().getStringExtra("color");
        String numberText = getIntent().getStringExtra("number");

        mark.setText(markText);
        name.setText(nameText != null ? nameText : "Нет закрепленного водителя");
        color.setText(colorText);
        number.setText(numberText);

        name.setOnClickListener(v -> {
            Intent intent = new Intent(this, DriverListActivity.class);
            intent.putExtra("manage", 2);
            intent.putExtra("bus_id", busId);
            startActivityForResult(intent, 0);
        });

        confirmEdits.setOnClickListener(v -> new UpdateBusTask(this).execute(String.valueOf(driverId), mark.getText().toString(), number.getText().toString(), color.getText().toString(), String.valueOf(busId)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            Client client = (Client) data.getSerializableExtra("driver");
            driverId = client.getId();
            name.setText("Водитель: " + client.getName());
        }
    }
}