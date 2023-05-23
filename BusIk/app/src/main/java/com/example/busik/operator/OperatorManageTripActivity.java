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
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.client.HistoryActivity;
import com.example.busik.client.SearchActivity;
import com.example.busik.other.ServerWork;
import com.example.busik.other.Trip;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OperatorManageTripActivity extends AppCompatActivity {

    private Spinner departureSpinner;
    private Spinner destinationSpinner;
    private TextView date;
    private TextView time;
    private TextView departureTime;
    private TextView destinationTime;
    private TextView departureCity;
    private TextView destinationCity;
    private TextView driver;

    private String dbDate;

    private List<String> departureList;
    private List<String> destinationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trip);

        Trip trip = (Trip) getIntent().getSerializableExtra("trip");
        String driverName = getIntent().getStringExtra("driver_name");
        fillLayout(trip, driverName);

        Client client = (Client) getIntent().getSerializableExtra("client");

    }

    public void fillLayout(Trip trip, String driverName)
    {
        departureSpinner = findViewById(R.id.departure_spinner);
        destinationSpinner = findViewById(R.id.destination_spinner);
        date = findViewById(R.id.date_text);
        time = findViewById(R.id.time_text);
        departureTime = findViewById(R.id.departure_time);
        destinationTime = findViewById(R.id.destination_time);
        departureCity = findViewById(R.id.departure_city);
        destinationCity = findViewById(R.id.destination_city);
        driver = findViewById(R.id.driver_name);


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

        departureTime.setText(trip.getStartTime());
        destinationTime.setText(trip.getEndTime());
        departureCity.setText(trip.getRoute().split("-")[0]);
        destinationCity.setText(trip.getRoute().split("-")[1]);
        time.setText(trip.getStartTime());
        driver.setText("Водитель: " + driverName);

        departureSpinner.setSelection(departureList.indexOf(departureCity.getText().toString()));
        destinationSpinner.setSelection(destinationList.indexOf(destinationCity.getText().toString()));



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

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
                intent.putExtra("manage", true);
                intent.putExtra("trip_id", trip.getId());
                startActivity(intent);
            });
        }
    }
}