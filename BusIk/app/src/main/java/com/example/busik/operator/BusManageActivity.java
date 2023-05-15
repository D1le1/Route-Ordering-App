package com.example.busik.operator;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;

public class BusManageActivity extends AppCompatActivity {

    private Button manageTrips;
    private Button manageDrivers;
    private Button manageApplications;
    private Button manageBuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);

        fillLayout();




    }

    public void fillLayout()
    {

    }
}