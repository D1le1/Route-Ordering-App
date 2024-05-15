package com.example.operatordesktop.controllers

import com.example.operatordesktop.HelloApplication
import com.example.operatordesktop.RootStage
import com.example.operatordesktop.util.Bus
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


class ManageBusesController {
    lateinit var busesList: TableView<Bus>
    lateinit var markCol: TableColumn<Bus, String>
    lateinit var numberCol: TableColumn<Bus, String>
    lateinit var colorCol: TableColumn<Bus, String>
    lateinit var driverCol: TableColumn<Bus, String>

    fun initialize() {
        busesList.placeholder = Label("Таблица пуста")
        CoroutineScope(Dispatchers.IO).launch {
            val response = ServerWork.sendRequest("BUSES--3") ?: return@launch
            val jsonArray = JSONArray(response)
            val buses = FXCollections.observableArrayList<Bus>()
            for (i in 0 until jsonArray.length()) {
                val obj = MyJSONObject(jsonArray.getJSONObject(i))
                val bus = obj.parseToBus()
                buses.add(bus)
            }
            busesList.items.setAll(buses)
            fillColumns()
        }
    }

    fun onAddBus() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("add-bus-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        val icon = Image(HelloApplication::class.java.getResourceAsStream("icons/app_icon-playstore.png"))
        fxmlLoader.getController<AddBusController>().setStage(stage)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.scene = scene
        stage.title = "Добавление авто"
        stage.icons.add(icon)
        stage.showAndWait()
        initialize()
    }

    fun onEditBus() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("edit-bus-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        val icon = Image(HelloApplication::class.java.getResourceAsStream("icons/app_icon-playstore.png"))
        val bus = busesList.selectionModel.selectedItem ?: return
        fxmlLoader.getController<EditBusController>().setData(stage, bus)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.scene = scene
        stage.title = "Изменение авто"
        stage.icons.add(icon)
        stage.showAndWait()
        initialize()
    }

    fun onDeleteBus() {
        val bus = busesList.selectionModel.selectedItem ?: return
        if (!confirmDelete()) return

        CoroutineScope(Dispatchers.IO).launch {
            ServerWork.sendRequest("DELETE--BUS--${bus.id}")
            busesList.items.remove(bus)
        }
    }

    fun onBackButtonClick() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("main-menu-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 700.0, 400.0)
        RootStage.stage.scene = scene
    }

    private fun confirmDelete(): Boolean {
        val confirmationDialog = Alert(AlertType.WARNING)
        confirmationDialog.title = "Подтверждение удаления"
        confirmationDialog.headerText = null
        confirmationDialog.contentText = "Вы уверены, что хотите удалить этот элемент?"

        val deleteButton = ButtonType("Удалить", ButtonData.APPLY)
        val cancelButton = ButtonType("Отмена", ButtonData.CANCEL_CLOSE)
        confirmationDialog.buttonTypes.setAll(deleteButton, cancelButton)

        val result = confirmationDialog.showAndWait()

        return result.isPresent && result.get() == deleteButton
    }

    private fun fillColumns() {
        markCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.mark)
        }
        numberCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.number)
        }
        colorCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.color)
        }
        driverCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.driverName)
        }
    }
}