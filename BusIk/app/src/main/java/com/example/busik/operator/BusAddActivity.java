package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.servertasks.AddBusTask;
import com.example.busik.servertasks.UpdateBusTask;

public class BusAddActivity extends AppCompatActivity {

    private TextView mark;
    private TextView name;
    private TextView color;
    private TextView number;
    private TextView error;

    private Button confirmEdits;

    private int driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);
        mark = findViewById(R.id.bus_mark);
        name = findViewById(R.id.driver_name);
        color = findViewById(R.id.bus_color);
        number = findViewById(R.id.bus_number);
        confirmEdits = findViewById(R.id.btn_confirm_edits);
        error = findViewById(R.id.error);

        driverId = getIntent().getIntExtra("driver_id", 0);


        name.setOnClickListener(v -> {
            Intent intent = new Intent(this, DriverListActivity.class);
            intent.putExtra("manage", 2);
            startActivityForResult(intent, 0);
        });


        confirmEdits.setOnClickListener(v ->
        {
            if(!mark.getText().toString().isEmpty() && !color.getText().toString().isEmpty() && !number.getText().toString().isEmpty())
                new AddBusTask(this).execute(mark.getText().toString(), number.getText().toString(), color.getText().toString(), String.valueOf(driverId));
            else {
                error.setText("Не все поля заполнены!");
                error.setVisibility(View.VISIBLE);
            }
        });



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