package com.example.busik.other;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.databinding.ActivityAuthBinding;
import com.example.busik.servertasks.AuthTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.example.busik.other.CryptoUtils;

public class AuthActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    private EditText login;
    private EditText password;
    private ActivityAuthBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fillLayout();

        CryptoUtils.loadConfig(this);
        ServerWork.ip = getSharedPreferences("settings", MODE_PRIVATE).getString("ip", "");
        ServerWork.connectToServer(ServerWork.ip);

        // Обработка нажатия на кнопку "Зарегистрироваться"
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // Обработка нажатия на кнопку "Авторизоваться"
        loginButton.setOnClickListener(v ->
                new AuthTask(this).execute(CryptoUtils.encrypt(login.getText().toString()), CryptoUtils.encrypt(password.getText().toString())));

        binding.logoImageView.setOnLongClickListener(v -> {
            Ut.showIpDialog(this);
            return false;
        });
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