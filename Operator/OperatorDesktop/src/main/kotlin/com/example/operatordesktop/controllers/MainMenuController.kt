package com.example.operatordesktop.controllers

import com.example.operatordesktop.HelloApplication
import com.example.operatordesktop.RootStage
import javafx.fxml.FXMLLoader
import javafx.scene.Scene

class MainMenuController {

    fun onManageTripsClick(){
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("manage-trips-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 960.0, 720.0)
        RootStage.stage.scene = scene
    }

    fun onManageApplyClick(){
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("manage-apply-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        RootStage.stage.scene = scene
    }

    fun onBackButtonClick(){
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        RootStage.stage.scene = scene
    }
}