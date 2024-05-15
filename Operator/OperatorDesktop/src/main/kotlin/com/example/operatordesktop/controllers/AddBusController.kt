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

class AddBusController {
    private var drivers = FXCollections.observableArrayList<Client>()
    private lateinit var stage: Stage

    lateinit var markText: TextField
    lateinit var numberText: TextField
    lateinit var colorText: TextField
    lateinit var driverCBox: ComboBox<String>
    lateinit var errorText: Label

    fun setStage(stage: Stage) {
        this.stage = stage
    }

    fun initialize() {

        driverCBox.items.add("Без водителя")
        CoroutineScope(Dispatchers.IO).launch {
            val response = ServerWork.sendRequest("DRIVERS--2")
            val array = JSONArray(response)
            for (i in 0 until array.length()) {
                val obj = MyJSONObject(array.getJSONObject(i))
                val driver = obj.parseToDriver()
                val warning = if (obj.has("mark")) "" else "(нет авто)"
                drivers.add(driver)
                driverCBox.items.add("${driver.name} $warning")
            }

        }
    }

    fun onAddButtonClick() {
        if (!validateFields())
            return

        CoroutineScope(Dispatchers.IO).launch {
            val mark = markText.text
            val number = numberText.text
            val color = colorText.text
            var driverId = -1
            try{
                driverId = drivers[driverCBox.selectionModel.selectedIndex - 1].id
            }catch (_:Exception){}
            val response = ServerWork.sendRequest("ADD--BUS--$mark--$number--$color--$driverId")
            CoroutineScope(Dispatchers.Main).launch {
                if(response != "ADD--OK"){
                    showError("Ошибка добавления. Возможно такое авто уже существует")
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
        if (markText.text.isEmpty()) {
            showError("Поле \"Марка\" не должно быть пустым")
            return false
        }
        if (numberText.text.isEmpty()) {
            showError("Поле \"Номер\" не должно быть пустым")
            return false
        }
        if (colorText.text.isEmpty()) {
            showError("Поле \"Цвет\" не должно быть пустым")
            return false
        }
        if (!Pattern.matches("[A-Z]{2}\\s\\d{4}-\\d", numberText.text)) {
            showError("Номер должен быть в формате AA 1111-1")
            return false
        }

        return true
    }

    private fun showError(text: String) {
        errorText.text = text
        errorText.isVisible = true
    }
}