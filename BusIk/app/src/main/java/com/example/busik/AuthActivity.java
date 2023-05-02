package com.example.busik;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.servertasks.AuthTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AuthActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    private EditText login;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        fillLayout();

        ServerWork.connectToServer();

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            new AuthTask(login.getText().toString(),this).execute();
        });

    }

    private void fillLayout()
    {
        registerButton = findViewById(R.id.register_button);
        loginButton = findViewById(R.id.login_button);
        login = findViewById(R.id.username_edit_text);
        title = findViewById(R.id.title);
    }

}