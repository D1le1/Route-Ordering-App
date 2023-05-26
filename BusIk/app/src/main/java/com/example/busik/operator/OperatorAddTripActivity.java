package com.example.busik.operator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.servertasks.AddTripTask;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OperatorAddTripActivity extends AppCompatActivity {

    private Spinner departureSpinner;
    private Spinner destinationSpinner;
    private TextView date;
    private TextView time;
    private TextView driver;

    private String dbDate;

    private List<String> departureList;
    private List<String> destinationList;

    private Button confirm;

    private int driverId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        fillLayout();

    }

    public void fillLayout()
    {
        departureSpinner = findViewById(R.id.departure_spinner);
        destinationSpinner = findViewById(R.id.destination_spinner);
        date = findViewById(R.id.date_text);
        time = findViewById(R.id.time_text);
        driver = findViewById(R.id.driver_name);
        confirm = findViewById(R.id.btn_confirm);


        // Fill the departure and destination spinner lists
        departureList = Arrays.asList("Минск", "Бобруйск", "Гомель");
        destinationList = Arrays.asList("Минск", "Бобруйск", "Гомель");

        ArrayAdapter<String> departureAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, departureList);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, destinationList);


        departureSpinner.setAdapter(departureAdapter);
        destinationSpinner.setAdapter(destinationAdapter);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM", new Locale("ru", "RU"));
        SimpleDateFormat dbDateFormat = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru", "RU"));
        date.setText(dateFormat.format(calendar.getTime()));
        dbDate = dbDateFormat.format(calendar.getTime());

        time.setText("12:00");
        driver.setText("Водитель не выбран");

        destinationSpinner.setSelection(1);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            int hour = 12;
            int minute = 0;

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, min) -> {
                time.setText(hourOfDay + ":" + String.format("%02d", min));
            }, hour, minute, DateFormat.is24HourFormat(this));

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, ((view, year, month, day) -> {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, day);

                date.setText(dateFormat.format(selectedDate.getTime()));
                dbDate = dbDateFormat.format(selectedDate.getTime());
            }), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            calendar.add(Calendar.WEEK_OF_MONTH, 2);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            date.setOnClickListener(v -> datePickerDialog.show());
            time.setOnClickListener(v -> timePickerDialog.show());

            driver.setOnClickListener(v -> {
                Intent intent = new Intent(this, DriverListActivity.class);
                intent.putExtra("manage", 1);
                startActivityForResult(intent, 0);
            });

            confirm.setOnClickListener(v -> {
                new AddTripTask(this).execute(
                        departureSpinner.getSelectedItem().toString() + "-" + destinationSpinner.getSelectedItem().toString(),
                        dbDate,
                        time.getText().toString(),
                        String.valueOf(driverId)
                );
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Client client = (Client) data.getSerializableExtra("driver");
            driver.setText("Водитель: " + client.getName());
            driverId = client.getId();
        }
    }
}