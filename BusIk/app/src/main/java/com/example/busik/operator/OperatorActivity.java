package com.example.busik.operator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.client.HistoryActivity;
import com.example.busik.client.SearchActivity;
import com.example.busik.other.ServerWork;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OperatorActivity extends AppCompatActivity {

    private Button manageTrips;
    private Button manageDrivers;
    private Button manageApplications;
    private Button manageBuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);


    }

    public void fillLayout()
    {

    }
}