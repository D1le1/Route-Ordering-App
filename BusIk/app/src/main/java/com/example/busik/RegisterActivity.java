package com.example.busik;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busik.servertasks.RegisterTask;

public class RegisterActivity extends AppCompatActivity {

    private Button register;

    private RadioGroup radioGroup;
    private RadioButton client;
    private RadioButton driver;
    private RadioButton operator;

    private EditText login;
    private EditText password;
    private EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fillLayout();

        register.setOnClickListener(v -> {
            RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
            if(radioButton != null)
            {
                new RegisterTask(this).execute(
                        login.getText().toString(),
                        AuthActivity.hashPassword(password.getText().toString()),
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
    }

}