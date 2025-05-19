package com.example.busik.other;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.busik.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Ut {
    public static void showIpDialog(Context context) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(context, R.style.dialogStyle);

        // Создаем EditText программно
        TextInputEditText editText = new TextInputEditText(context);
        editText.setId(View.generateViewId());
        editText.setText(ServerWork.ip);
        editText.setHint("Введите IP-адрес");
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Создаем TextInputLayout (обертка для EditText в Material Design)
        TextInputLayout textInputLayout = new TextInputLayout(context);
        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textInputLayout.addView(editText);

        // Создаем контейнер и добавляем в него TextInputLayout
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(64, 32, 64, 32);
        container.addView(textInputLayout);

        alertDialogBuilder.setTitle("Настройка IP");
        alertDialogBuilder.setView(container); // Добавляем наш контейнер с EditText
        alertDialogBuilder.setPositiveButton("Применить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                String ipAddress = editText.getText().toString();
                sp.edit().putString("ip", ipAddress).apply();
                ServerWork.ip = ipAddress;
            }
        });
        alertDialogBuilder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
