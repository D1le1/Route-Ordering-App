package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.client.Client;

public class OperatorManageDriverActivity extends AppCompatActivity {

    private Button manageTrips;
    private Button manageDrivers;
    private Button manageApplications;
    private Button manageBuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_driver);

        fillLayout();




    }

    public void fillLayout()
    {

    }
}