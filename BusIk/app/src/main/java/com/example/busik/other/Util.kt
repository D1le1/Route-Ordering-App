package com.example.busik.other

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.busik.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Util {
    companion object{
        fun showIpDialog(context: Context) {
            val alertDialogBuilder = MaterialAlertDialogBuilder(context, R.style.dialogStyle)

            // Создаем EditText программно
            val editText = TextInputEditText(context).apply {
                id = View.generateViewId()
                hint = "Введите IP-адрес"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            // Создаем TextInputLayout (обертка для EditText в Material Design)
            val textInputLayout = TextInputLayout(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                addView(editText)
            }

            // Создаем контейнер и добавляем в него TextInputLayout
            val container = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
//            setPadding(
//                resources.getDimensionPixelSize(R.dimen.dialog_padding),
//                resources.getDimensionPixelSize(R.dimen.dialog_padding),
//                resources.getDimensionPixelSize(R.dimen.dialog_padding),
//                resources.getDimensionPixelSize(R.dimen.dialog_padding)
//            )
                addView(textInputLayout)
            }

            alertDialogBuilder.apply {
                setTitle("Настройка IP")
                setView(container) // Добавляем наш контейнер с EditText
                setPositiveButton("Применить") { _, _ ->
                    val ipAddress = editText.text.toString()
                }
                setNegativeButton("Отменить") { dialog, _ ->
                    dialog.dismiss()
                }
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

}