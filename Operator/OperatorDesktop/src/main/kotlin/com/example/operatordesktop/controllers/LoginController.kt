package com.example.operatordesktop.controllers

import com.example.operatordesktop.HelloApplication
import com.example.operatordesktop.RootStage
import com.example.operatordesktop.util.CryptoUtils
import com.example.operatordesktop.util.MyJSONObject
import com.example.operatordesktop.util.ServerWork
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginController {

    lateinit var messageText: Label
    lateinit var loginText: TextField
    lateinit var passwordText: PasswordField
    lateinit var loginButton: Button
    lateinit var regButton: Button

    fun onLoginButtonClick() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ServerWork.sendRequest("AUTH--${CryptoUtils.encrypt(loginText.text)}--${CryptoUtils.encrypt(passwordText.text)}")
            if (response == null) {
                CoroutineScope(Dispatchers.Main).launch {
                    messageText.text = "Нет соеденения с сервером"
                }
                return@launch
            }
            try {
                val obj = MyJSONObject(response)
                val client = obj.parseToClient()
                val apply = obj.get("apply")
                if (apply != 1) {
                    CoroutineScope(Dispatchers.Main).launch {
                        messageText.text = "Не одобрен администратором"
                    }
                    return@launch
                }
                if (client.role != 3) {
                    CoroutineScope(Dispatchers.Main).launch {
                        messageText.text = "Пользователь не найден"
                    }
                    return@launch
                }
                CoroutineScope(Dispatchers.Main).launch {
                    val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("main-menu-view.fxml"))
                    val scene = Scene(fxmlLoader.load(), 700.0, 400.0)
                    RootStage.stage.scene = scene

                }
            } catch (_: JSONException) {
                CoroutineScope(Dispatchers.Main).launch {
                    messageText.text = "Пользователь не найден"
                }
            }
        }
    }

    fun onRegButtonClick() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("reg-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 700.0, 500.0)
        RootStage.stage.scene = scene
    }
}