package com.example.operatordesktop.controllers

import com.example.operatordesktop.HelloApplication
import com.example.operatordesktop.RootStage
import com.example.operatordesktop.util.Client
import com.example.operatordesktop.util.CryptoUtils
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


class ManageApplyController {
    lateinit var appliesList: TableView<Client>
    lateinit var nameCol: TableColumn<Client, String>
    lateinit var postCol: TableColumn<Client, String>
    lateinit var numberCol: TableColumn<Client, String>

    fun initialize() {
        appliesList.placeholder = Label("Таблица пуста")
        CoroutineScope(Dispatchers.IO).launch {
            val response = ServerWork.sendRequest("APPLICATION--LIST") ?: return@launch
            val jsonArray = JSONArray(response)
            val clients = FXCollections.observableArrayList<Client>()
            for (i in 0 until jsonArray.length()) {
                val obj = MyJSONObject(jsonArray.getJSONObject(i))
                clients.add(obj.parseToApplyClient())
            }
            appliesList.items = clients
            fillColumns()
        }
    }

    fun onApply(){
        val item = appliesList.selectionModel.selectedItem
        CoroutineScope(Dispatchers.IO).launch {
            ServerWork.sendRequest("APPLICATION--${item.id}--1")
            appliesList.items.remove(item)
        }
    }
    fun onDecline(){
        val item = appliesList.selectionModel.selectedItem
        CoroutineScope(Dispatchers.IO).launch {
            ServerWork.sendRequest("APPLICATION--${item.id}--0")
            appliesList.items.remove(item)
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
        nameCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(it.value.name)
        }
        postCol.setCellValueFactory {
            when(it.value.role){
                2 -> return@setCellValueFactory SimpleStringProperty("Водитель")
                3 -> return@setCellValueFactory SimpleStringProperty("Оператор")
            }
            return@setCellValueFactory SimpleStringProperty("Пользователь")
        }
        numberCol.setCellValueFactory {
            return@setCellValueFactory SimpleStringProperty(CryptoUtils.decrypt(it.value.phone))
        }
    }
}