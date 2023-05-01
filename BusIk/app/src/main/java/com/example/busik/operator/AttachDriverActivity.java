package com.example.busik.operator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.busik.R;

import java.util.ArrayList;
import java.util.List;

public class AttachDriverActivity extends AppCompatActivity {

    private TextView mTextRoute;
    private Button mBtnAttach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_driver);

    }
}