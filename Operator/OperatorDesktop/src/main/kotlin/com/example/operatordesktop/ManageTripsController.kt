package com.example.operatordesktop

import com.example.operatordesktop.util.MyJSONObject
import com.example.operatordesktop.util.ServerWork
import com.example.operatordesktop.util.Trip
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.control.ButtonType
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray


class ManageTripsController {
    lateinit var tripsList: TableView<Trip>
    lateinit var routeCol: TableColumn<Trip, String>
    lateinit var driverCol: TableColumn<Trip, String>
    lateinit var dateCol: TableColumn<Trip, String>
    lateinit var timeCol: TableColumn<Trip, String>
    lateinit var finishCol: TableColumn<Trip, String>

    fun initialize() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ServerWork.sendRequest("TRIPS--0--3") ?: return@launch
            val jsonArray = JSONArray(response)
            val trips = FXCollections.observableArrayList<Trip>()
            for (i in 0 until jsonArray.length()) {
                val obj = MyJSONObject(jsonArray.getJSONObject(i))
                trips.add(obj.parseToTrip())
            }
            tripsList.items.setAll(trips)
            fillColumns()
        }
    }

    fun onDeleteTrip() {
        val trip = tripsList.selectionModel.selectedItem ?: return
        if (!confirmDelete())
            return

        CoroutineScope(Dispatchers.IO).launch {
            ServerWork.sendRequest("DELETE--TRIP--${trip.id}")
            tripsList.items.remove(trip)
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

        if (result.isPresent && result.get() == deleteButton)
            return true
        return false
    }

    private fun fillColumns() {
        routeCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.route)
        }
        driverCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.driverName)
        }
        dateCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.date)
        }
        timeCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.startTime)
        }
        finishCol.setCellValueFactory {
            val value = if (it.value.finished == 1) "Завершено" else "Активен"
            return@setCellValueFactory SimpleStringProperty(value)
        }
    }
}