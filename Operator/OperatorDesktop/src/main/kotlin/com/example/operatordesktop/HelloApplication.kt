package com.example.operatordesktop

import com.example.operatordesktop.util.CryptoUtils
import com.example.operatordesktop.util.ServerWork
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import java.io.File
import java.util.regex.Pattern

object RootStage {
    lateinit var stage: Stage
}

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val path = "src/main/resources/com/example/operatordesktop/properties.keys"
        CryptoUtils.loadFromProperties(path)

        RootStage.stage = stage
        stage.isResizable = false
        val fxmlLoader = FXMLLoader(javaClass.getResource("login-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        val icon = Image(javaClass.getResourceAsStream("icons/app_icon-playstore.png"))
        stage.title = "BusIk Operator"
        stage.scene = scene
        stage.icons.add(icon)
        stage.show()
        ServerWork.connectToServer()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}