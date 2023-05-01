package com.example.busik.client;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ClientActivity extends AppCompatActivity {

    private Spinner departureSpinner;
    private Spinner destinationSpinner;
    private Spinner passengersSpinner;
    private Button searchButton;
    private TextView date;

    private List<String> departureList;
    private List<String> destinationList;
    private List<String> passengerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        departureSpinner = findViewById(R.id.departure_spinner);
        destinationSpinner = findViewById(R.id.destination_spinner);
        searchButton = findViewById(R.id.select_button);
        passengersSpinner = findViewById(R.id.passenger_spinner);
        date = findViewById(R.id.date_text);

        // Fill the departure and destination spinner lists
        departureList = Arrays.asList("Минск", "Бобруйск", "Гомель");
        destinationList = Arrays.asList("Минск", "Бобруйск", "Гомель");
        passengerList = Arrays.asList("Пассажир 1", "Пассажир 2", "Пассажир 3", "Пассажир 4", "Пассажир 5");

        ArrayAdapter<String> departureAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, departureList);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, destinationList);
        ArrayAdapter<String> passengerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, passengerList);


        departureSpinner.setAdapter(departureAdapter);
        destinationSpinner.setAdapter(destinationAdapter);
        passengersSpinner.setAdapter(passengerAdapter);

        // Set up the search button onClickListener
        searchButton.setOnClickListener(v -> {
                String departure = departureSpinner.getSelectedItem().toString();
                String destination = destinationSpinner.getSelectedItem().toString();

        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM", new Locale("ru", "RU"));
        String startDate = dateFormat.format(calendar.getTime());
        date.setText(startDate);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ClientActivity.this, R.style.MyDatePickerDialogStyle, ((view, year1, month1, dayOfMonth) -> {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year1);
                selectedDate.set(Calendar.MONTH, month1);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String formattedDate = dateFormat.format(selectedDate.getTime());

                date.setText(formattedDate);
            }), year, month, day);

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            calendar.add(Calendar.WEEK_OF_MONTH, 2);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            date.setOnClickListener(v -> {
                datePickerDialog.show();
            });
        }
    }

    private void performSearch(String departure, String destination, int passengers, String date) {
        // Perform the search based on the selected trip information
        // ...
    }
}