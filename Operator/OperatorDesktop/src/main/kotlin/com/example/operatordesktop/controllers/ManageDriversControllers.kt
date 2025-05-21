package com.example.operatordesktop.controllers

import com.example.operatordesktop.HelloApplication
import com.example.operatordesktop.RootStage
import com.example.operatordesktop.util.Client
import com.example.operatordesktop.util.MyJSONObject
import com.example.operatordesktop.util.ServerWork
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray


class ManageDriversControllers {
    lateinit var driversList: TableView<Client>
    lateinit var markCol: TableColumn<Client, String>
    lateinit var phoneCol: TableColumn<Client, String>
    lateinit var nameCol: TableColumn<Client, String>

    fun initialize() {
        driversList.placeholder = Label("Таблица пуста")
        CoroutineScope(Dispatchers.IO).launch {
            val response = ServerWork.sendRequest("DRIVERS--3") ?: return@launch
            val jsonArray = JSONArray(response)
            val drivers = FXCollections.observableArrayList<Client>()
            for (i in 0 until jsonArray.length()) {
                val obj = MyJSONObject(jsonArray.getJSONObject(i))
                val driver = obj.parseToDriver()
                drivers.add(driver)
            }
            driversList.items.setAll(drivers)
            fillColumns()
        }
    }

    fun onEditClick(){
        val driver = driversList.selectionModel.selectedItem ?: return
        val stage = Stage()
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("edit-driver-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        val icon = Image(HelloApplication::class.java.getResourceAsStream("icons/app_icon-playstore.png"))
        fxmlLoader.getController<EditDriverController>().setData(stage, driver)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.scene = scene
        stage.title = "Изменение водителя"
        stage.icons.add(icon)
        stage.showAndWait()
        initialize()
    }

    fun onBackButtonClick() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("main-menu-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 700.0, 400.0)
        RootStage.stage.scene = scene
    }

    private fun fillColumns() {
        markCol.setCellValueFactory {
            if(it.value.bus.size > 1)
                return@setCellValueFactory SimpleStringProperty(it.value.bus[1])
            else
                return@setCellValueFactory SimpleStringProperty(it.value.bus[0])
        }
        phoneCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.phone)
        }
        nameCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.name)
        }
    }
}