package com.example.operatordesktop.controllers

import com.example.operatordesktop.HelloApplication
import com.example.operatordesktop.RootStage
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
import javafx.scene.control.Label
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray


class ManageTripsController {
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val backScope = CoroutineScope(Dispatchers.IO)

    lateinit var tripsList: TableView<Trip>
    lateinit var routeCol: TableColumn<Trip, String>
    lateinit var driverCol: TableColumn<Trip, String>
    lateinit var dateCol: TableColumn<Trip, String>
    lateinit var timeCol: TableColumn<Trip, String>

    fun initialize() {
        tripsList.placeholder = Label("Таблица пуста")
        CoroutineScope(Dispatchers.IO).launch {
            val response = ServerWork.sendRequest("TRIPS--0--3") ?: return@launch
            val jsonArray = JSONArray(response)
            val trips = FXCollections.observableArrayList<Trip>()
            for (i in 0 until jsonArray.length()) {
                val obj = MyJSONObject(jsonArray.getJSONObject(i))
                val trip = obj.parseToTrip()
                val busNumber = if(obj.has("bus_number")) " (${obj.getString("bus_number")})" else ""
                trip.driverName += if (!obj.has("bus_number") && obj.get("driver_id") as Int > 0) " (нет авто)" else busNumber
                trips += trip
            }
            mainScope.launch {
                tripsList.items.setAll(trips)
            }
            tripsList.selectionModel.selectionMode = SelectionMode.MULTIPLE
            fillColumns()
        }
    }

    fun onAddTrip() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("add-trip-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        val icon = Image(HelloApplication::class.java.getResourceAsStream("icons/app_icon-playstore.png"))
        fxmlLoader.getController<AddTripController>().setStage(stage)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.scene = scene
        stage.title = "Добавление рейса"
        stage.icons.add(icon)
        stage.showAndWait()
        initialize()
    }

    fun onEditTrip() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("edit-trip-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        val icon = Image(HelloApplication::class.java.getResourceAsStream("icons/app_icon-playstore.png"))
        val trip = tripsList.selectionModel.selectedItem ?: return
        fxmlLoader.getController<EditTripController>().setData(stage, trip)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.scene = scene
        stage.title = "Изменение рейса"
        stage.icons.add(icon)
        stage.showAndWait()
        initialize()
    }

    fun onDeleteTrip() {
        val trips = tripsList.selectionModel.selectedItems ?: return
        if (!confirmDelete()) return

        CoroutineScope(Dispatchers.IO).launch {
            for(i in trips.size - 1 downTo 0){
                ServerWork.sendRequest("DELETE--TRIP--${trips[i].id}")
                tripsList.items.remove(trips[i])
            }
        }
    }

    fun onCreateSch() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("create-schedule-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        val icon = Image(HelloApplication::class.java.getResourceAsStream("icons/app_icon-playstore.png"))
        fxmlLoader.getController<CreateScheduleController>().setStage(stage)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.scene = scene
        stage.title = "Создание цепочки рейсов"
        stage.icons.add(icon)
        stage.showAndWait()
        initialize()
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
        confirmationDialog.contentText = "Вы уверены, что хотите удалить этот(эти) элемент(ы)?"

        val deleteButton = ButtonType("Удалить", ButtonData.APPLY)
        val cancelButton = ButtonType("Отмена", ButtonData.CANCEL_CLOSE)
        confirmationDialog.buttonTypes.setAll(deleteButton, cancelButton)

        val result = confirmationDialog.showAndWait()

        return result.isPresent && result.get() == deleteButton
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
            return@setCellValueFactory SimpleStringProperty("${it.value.startTime} - ${it.value.endTime}")
        }
    }
}