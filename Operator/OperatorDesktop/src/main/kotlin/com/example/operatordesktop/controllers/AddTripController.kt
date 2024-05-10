package com.example.operatordesktop.controllers

import com.example.operatordesktop.RootStage
import com.example.operatordesktop.util.Client
import com.example.operatordesktop.util.MyJSONObject
import com.example.operatordesktop.util.ServerWork
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.DateCell
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.lang.Exception
import java.nio.channels.DatagramChannel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

class AddTripController {
    private val destinations = FXCollections.observableArrayList("Минск", "Бобруйск", "Гомель")
    private var fromValue = ""
    private var whereValue = ""
    private var drivers = FXCollections.observableArrayList<Client>()
    private lateinit var stage: Stage

    lateinit var fromCBox: ComboBox<String>
    lateinit var whereCBox: ComboBox<String>
    lateinit var driverCBox: ComboBox<String>
    lateinit var datePicker: DatePicker
    lateinit var timeText: TextField
    lateinit var errorText: Label

    fun setStage(stage: Stage) {
        this.stage = stage
    }

    fun initialize() {
        fromCBox.items = destinations
        whereCBox.items = destinations
        fromCBox.setOnAction { checkComboValues() }
        whereCBox.setOnAction { checkComboValues() }

        driverCBox.items.add("Без водителя")
        CoroutineScope(Dispatchers.IO).launch {
            val response = ServerWork.sendRequest("DRIVERS--3")
            val array = JSONArray(response)
            for (i in 0 until array.length()) {
                val obj = MyJSONObject(array.getJSONObject(i))
                val driver = obj.parseToDriver()
                drivers.add(driver)
                driverCBox.items.add(driver.name)
            }

        }

        datePicker.setDayCellFactory {
            object : DateCell() {
                override fun updateItem(item: LocalDate?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (item != null) {
                        isDisable = item.isBefore(LocalDate.now()) || item.isAfter(LocalDate.now().plusDays(7))
                    }
                }
            }
        }
    }

    fun onAddButtonClick() {
        if (!validateFields())
            return

        CoroutineScope(Dispatchers.IO).launch {
            val route = "${fromCBox.selectionModel.selectedItem}-${whereCBox.selectionModel.selectedItem}"
            val date = datePicker.value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            val time = timeText.text
            var driverId = -1
            try {
                driverId = drivers[driverCBox.selectionModel.selectedIndex - 1].id
            }catch (_:Exception){}
            val response = ServerWork.sendRequest("ADD--TRIP--$route--$date--$time--$driverId")

            CoroutineScope(Dispatchers.Main).launch {
                if(response != "ADD--OK"){
                    showError("Ошибка добавления. Возможно такой рейс уже существует")
                    return@launch
                }
                stage.close()
            }
        }
    }

    fun onBackButtonClick() {
        stage.close()
    }

    private fun validateFields(): Boolean {
        if (fromValue.isEmpty()) {
            showError("Поле \"Откуда\" не должно быть пустым")
            return false
        }
        if (whereValue.isEmpty()) {
            showError("Поле \"Куда\" не должно быть пустым")
            return false
        }
        if (datePicker.value == null) {
            showError("Поле \"Дата\" не должно быть пустым")
            return false
        }
        if (timeText.text.isEmpty()) {
            showError("Поле \"Время\" не должно быть пустым")
            return false
        }
        if (!Pattern.matches("\\d\\d:\\d\\d", timeText.text)) {
            showError("Формат времени (12:30)")
            return false
        }

        return true
    }

    private fun checkComboValues() {
        if (fromCBox.value != whereCBox.value) {
            fromValue = fromCBox.value ?: ""
            whereValue = whereCBox.value ?: ""
            return
        }
        if (fromCBox.isShowing) {
            whereCBox.value = fromValue
        } else
            fromCBox.value = whereValue

        fromValue = fromCBox.value
        whereValue = whereCBox.value
    }

    private fun showError(text: String) {
        errorText.text = text
        errorText.isVisible = true
    }
}