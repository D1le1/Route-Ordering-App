package com.example.busik.other;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.R;
import com.example.busik.servertasks.RegisterTask;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button register;

    private RadioGroup radioGroup;

    private EditText login;
    private EditText password;
    private EditText userName;

    private TextView error;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fillLayout();

        register.setOnClickListener(v -> {
            RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
            if(validation(radioButton))
            {
                new RegisterTask(this).execute(
                        CryptoUtils.encrypt(login.getText().toString()),
                        CryptoUtils.encrypt(password.getText().toString()),
                        userName.getText().toString(),
                        radioButton.getTag().toString()
                );
            }

        });

    }

    private void fillLayout()
    {
        register = findViewById(R.id.register_button);
        radioGroup = findViewById(R.id.role_radio_group);
        login = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
        userName = findViewById(R.id.user_name);
        error = findViewById(R.id.error);
    }

    private boolean validation(RadioButton radioButton)
    {
        boolean result = true;
        error.setVisibility(View.INVISIBLE);
        if(radioButton == null) {
            error.setVisibility(View.VISIBLE);
            result = false;
        }
        String error = "Поле не должно быть пустым";
        if(login.getText().toString().isEmpty()) {
            login.setError(error);
            result = false;
//        }else if(login.getText().toString().length() != 12 && login.getText().toString()){
        }else if(!Pattern.matches("[3][7][5]\\d{9}", login.getText().toString())){
            login.setError("Номер должен состоять из 12 цифр (код 375)");
            result = false;
        }
        if(password.getText().toString().isEmpty())
        {
            password.setError(error);
            result = false;
        }else if(password.getText().toString().length() < 6)
        {
            password.setError("Пароль должен содержать не меньше 6 символов");
            result = false;
        }
        if(userName.getText().toString().isEmpty())
        {
            userName.setError(error);
            result = false;
        }
        return result;
    }

}