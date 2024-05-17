package com.example.operatordesktop.controllers

import com.example.operatordesktop.util.Bus
import com.example.operatordesktop.util.Client
import com.example.operatordesktop.util.MyJSONObject
import com.example.operatordesktop.util.ServerWork
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class EditDriverController {
    private var buses = FXCollections.observableArrayList<Bus>()
    private lateinit var stage: Stage
    private lateinit var driver: Client

    lateinit var busCBox: ComboBox<String>
    lateinit var errorText: Label
    lateinit var nameText: TextField
    lateinit var colorText: TextField
    lateinit var numberText: TextField
    lateinit var phoneText: TextField

    fun setData(stage: Stage, driver: Client) {
        this.stage = stage
        this.driver = driver

        nameText.text = driver.name
        phoneText.text = driver.phone

        if (driver.bus.size > 1) {
            busCBox.value = driver.bus[1]
            colorText.text = driver.bus[0]
            numberText.text = driver.bus[2]
            println(driver.bus[3])
        }
    }

    fun initialize() {

        busCBox.items.add("Без авто")
        CoroutineScope(Dispatchers.IO).launch {
            val response = ServerWork.sendRequest("BUSES--2")
            val array = JSONArray(response)
            for (i in 0 until array.length()) {
                val obj = MyJSONObject(array.getJSONObject(i))
                val bus = obj.parseToBus()
                buses.add(bus)
                busCBox.items.add(bus.mark)
            }
        }

        busCBox.setOnAction {
            try {
                val bus = buses[busCBox.selectionModel.selectedIndex - 1]
                colorText.text = bus.color
                numberText.text = bus.number
            } catch (_: Exception) {
                colorText.clear()
                numberText.clear()
            }
        }
    }

    fun onEditButtonClick() {
        if (!validateFields())
            return

        CoroutineScope(Dispatchers.IO).launch {
            val name = nameText.text
            val phone = phoneText.text
            val driverId = driver.id
            var busId = -1
            var curBusId = -1
            if(driver.bus.size > 1){
                curBusId = driver.bus[3].toInt()
                busId = curBusId
            }
            try {
                busId = if(busCBox.selectionModel.selectedIndex == 0)
                    -1
                else
                    buses[busCBox.selectionModel.selectedIndex - 1].id
            } catch (_: Exception) {
            }
            val response = ServerWork.sendRequest("UPDATE--DRIVER--$name--$phone--$driverId--$busId--$curBusId")
            CoroutineScope(Dispatchers.Main).launch {
                if (response != "UPDATE--OK") {
                    showError("Ошибка изменения. Возможно такое авто уже существует")
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
        if (nameText.text.isEmpty()) {
            showError("Поле \"Имя\" не должно быть пустым")
            return false
        }
        if (phoneText.text.isEmpty()) {
            showError("Поле \"Номер\" не должно быть пустым")
            return false
        }

        return true
    }

    private fun showError(text: String) {
        errorText.text = text
        errorText.isVisible = true
    }
}