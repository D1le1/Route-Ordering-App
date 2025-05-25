package com.example.operatordesktop.controllers

import com.example.operatordesktop.HelloApplication
import com.example.operatordesktop.RootStage
import com.example.operatordesktop.util.CryptoUtils
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
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern


class RegistrationController {
    lateinit var messageText: Label
    lateinit var loginText: TextField
    lateinit var fioText: TextField
    lateinit var passwordText: PasswordField
    lateinit var regButton: Button
    lateinit var backButton: Button

    fun onRegButtonClick() {
        CoroutineScope(Dispatchers.IO).launch {
            if(!validateTextFields())
                return@launch

            val response = ServerWork.sendRequest("REG--${CryptoUtils.encrypt(loginText.text)}--${CryptoUtils.encrypt(passwordText.text)}--${fioText.text}--3")
            if (response == null) {
                showMessage("Нет подключения к серверу")
                return@launch
            }
            if (response == "REG--EXISTS") {
                showMessage("Пользователь уже зарегистрирован")
                return@launch
            }
            if(response == "REG--DENY") {
                showMessage("Ошибка записи, попробуйте позже")
                return@launch
            }

            CoroutineScope(Dispatchers.Main).launch {
                val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login-view.fxml"))
                val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
                RootStage.stage.scene = scene
            }
        }
    }

    fun onBackButtonClick() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        RootStage.stage.scene = scene
    }

    private fun validateTextFields(): Boolean{
        if(fioText.text.isEmpty()){
            showMessage("Поле \"ФИ\" не должно быть пустым")
            return false
        }
        if(loginText.text.isEmpty()){
            showMessage("Поле \"логин\" не должно быть пустым")
            return false
        }
        if(passwordText.text.isEmpty()){
            showMessage("Поле \"пароль\" не должно быть пустым")
            return false
        }
        if(!Pattern.matches("[3][7][5]\\d{9}", loginText.text)){
            showMessage("Номер должен состоять из 12 цифр (код 375)")
            return false
        }
        if(passwordText.text.length < 6)
        {
            showMessage("Пароль должен содержать не менее 6 символов")
            return false
        }

        return true
    }

    private fun showMessage(text: String) {
        CoroutineScope(Dispatchers.Main).launch {
            messageText.text = text
        }
    }
}