package com.example.operatordesktop

import javafx.fxml.FXMLLoader
import javafx.scene.Scene

class MainMenuController {

    fun onManageTripsClick(){
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("manage-trips-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        RootStage.stage.scene = scene
    }
    fun onBackButtonClick(){
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        RootStage.stage.scene = scene
    }
}