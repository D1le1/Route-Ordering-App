package com.example.busik;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.servertasks.AuthTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class AuthActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    private EditText login;
    private EditText password;

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

        loginButton.setOnClickListener(v ->
                new AuthTask(this).execute(login.getText().toString(), hashPassword(password.getText().toString())));
    }

    private void fillLayout()
    {
        registerButton = findViewById(R.id.register_button);
        loginButton = findViewById(R.id.login_button);
        login = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
        title = findViewById(R.id.title);
    }

    public static String hashPassword(String password) {
        if(!password.equals("")) {
            try {
                // Получаем экземпляр класса MessageDigest с алгоритмом SHA-256
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

                // Преобразуем пароль в байтовый массив
                byte[] passwordBytes = password.getBytes();

                // Вычисляем хэш-код пароля
                byte[] hashBytes = messageDigest.digest(passwordBytes);

                // Преобразуем хэш-код в шестнадцатеричную строку
                StringBuilder stringBuilder = new StringBuilder();
                for (byte b : hashBytes) {
                    stringBuilder.append(String.format("%02x", b));
                }
                return stringBuilder.toString();
            } catch (NoSuchAlgorithmException e) {
                // Обработка исключения
                e.printStackTrace();
                return null;
            }
        }else
        {
            return " ";
        }
    }

}