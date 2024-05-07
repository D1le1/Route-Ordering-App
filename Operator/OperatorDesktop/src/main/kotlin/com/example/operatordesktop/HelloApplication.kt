package com.example.operatordesktop

import com.example.operatordesktop.util.ServerWork
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

object RootStage{
   lateinit var stage: Stage
}
class HelloApplication : Application() {
    override fun start(stage: Stage) {
        RootStage.stage = stage
        stage.isResizable = false
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        stage.title = "BusIk Operator"
        stage.scene = scene
        stage.show()
        ServerWork.connectToServer()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}