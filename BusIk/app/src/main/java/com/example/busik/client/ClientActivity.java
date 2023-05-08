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

        fillLayout();

    }

    public void fillLayout()
    {
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

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM", new Locale("ru", "RU"));
        date.setText(dateFormat.format(calendar.getTime()));


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, ((view, year, month, day) -> {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, day);

                date.setText(dateFormat.format(selectedDate.getTime()));
            }), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            calendar.add(Calendar.WEEK_OF_MONTH, 2);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            date.setOnClickListener(v -> {
                datePickerDialog.show();
            });
        }
    }
}