package com.example.busik.other;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.servertasks.AuthTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        fillLayout();

        // Подключение к серверу
        ServerWork.connectToServer();

        // Обработка нажатия на кнопку "Зарегистрироваться"
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // Обработка нажатия на кнопку "Авторизоваться"
        loginButton.setOnClickListener(v ->
                new AuthTask(this).execute(login.getText().toString(), hashPassword(password.getText().toString())));
    }

    // Метод заполнения объектов страницы
    private void fillLayout()
    {
        registerButton = findViewById(R.id.register_button);
        loginButton = findViewById(R.id.login_button);
        login = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
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