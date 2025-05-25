package com.example.operatordesktop.controllers

import com.example.operatordesktop.util.Client
import com.example.operatordesktop.util.MyJSONObject
import com.example.operatordesktop.util.ServerWork
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CreateScheduleController {
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val backScope = CoroutineScope(Dispatchers.IO)

    private val destinations = FXCollections.observableArrayList("Минск", "Бобруйск", "Гомель")
    private var fromValue = ""
    private var whereValue = ""
    private var drivers = FXCollections.observableArrayList<Client>()
    private lateinit var stage: Stage
    private lateinit var driver: Client
    private var routeTime = 0.0

    lateinit var fromCBox: ComboBox<String>
    lateinit var whereCBox: ComboBox<String>
    lateinit var driverCBox: ComboBox<String>
    lateinit var tripsCountCBox: ComboBox<Int>
    lateinit var datePicker: DatePicker
    lateinit var timeText: TextField
    lateinit var errorText: Label

    fun setStage(stage: Stage) {
        this.stage = stage
    }

    fun setParams(stage: Stage, driver: Client) {
        this.stage = stage
        this.driver = driver

        driverCBox.selectionModel.select(drivers.indexOfFirst { it.id == driver.id })
    }

    fun initialize() {
        for (i in 1..10)
            tripsCountCBox.items.add(i)
        fromCBox.items = destinations
        whereCBox.items = destinations
        fromCBox.setOnAction { checkComboValues() }
        whereCBox.setOnAction { checkComboValues() }

        CoroutineScope(Dispatchers.IO).launch {
            val response = ServerWork.sendRequest("DRIVERS--3")
            val array = JSONArray(response)
            for (i in 0 until array.length()) {
                val obj = MyJSONObject(array.getJSONObject(i))
                val driver = obj.parseToDriver()
                val warning = if (obj.has("mark")) "" else " (нет авто)"
                driver.name = "${driver.name}$warning"
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

        tripsCountCBox.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            validateTripsCount()
        }

    }

    fun onCreateButtonClick() {
        if (!validateFields())
            return

        CoroutineScope(Dispatchers.IO).launch {
            if (!validateTripsCount())
                return@launch

            val firstRoute = "${fromCBox.selectionModel.selectedItem}-${whereCBox.selectionModel.selectedItem}"
            val secondRoute = "${whereCBox.selectionModel.selectedItem}-${fromCBox.selectionModel.selectedItem}"
            val count = tripsCountCBox.selectionModel.selectedItem

            val date = datePicker.value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            val startTime = LocalTime.parse(timeText.text, DateTimeFormatter.ofPattern("HH:mm"))
            var driverId = -1
            try {
                driverId = drivers[driverCBox.selectionModel.selectedIndex].id
            } catch (_: Exception) {
            }

            println("RouteTime: $routeTime")
            for (i in 0 until count) {
                val time = startTime.addDouble(i * (routeTime + 1))
                val route = if (i % 2 == 0) firstRoute else secondRoute
                val response = ServerWork.sendRequest("ADD--TRIP--$route--$date--$time--$driverId")
                println("Response: $response")
                if(response != "ADD--OK"){
                    mainScope.launch {
                        showError("Ошибка при добавлении рейсов. Попробуйте еще раз!")
                    }
                    return@launch
                }
            }
            mainScope.launch {
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
        try {
            LocalTime.parse(timeText.text, DateTimeFormatter.ofPattern("HH:mm"))
        } catch (_: kotlin.Exception) {
            showError("Неверный формат времени! (Корректный формат: 08:30)")
            return false
        }
        if (tripsCountCBox.value == null){
            showError("Выберите количество рейсов!")
            return false
        }
        if (driverCBox.value == null) {
            showError("Выберите водителя")
            return false
        }

        return true
    }

    private fun validateTripsCount(): Boolean {
        try {
            val route = "${fromCBox.selectionModel.selectedItem}-${whereCBox.selectionModel.selectedItem}"
            val json = JSONObject(ServerWork.sendRequest("ROUTE--$route"))
            val startTime =
                LocalDateTime.of(datePicker.value, LocalTime.parse(timeText.text, DateTimeFormatter.ofPattern("HH:mm")))
            routeTime = json.getDouble("time")
            val lastTime =
                startTime.addDouble((tripsCountCBox.selectionModel.selectedItem - 1) * (routeTime + 1))
            println(lastTime)
            if (lastTime.isAfter(LocalDateTime.of(datePicker.value, LocalTime.parse("22:00")))) {
                mainScope.launch {
                    showError(
                        "Нельзя, чтобы последний рейс был позже 22:00 (Сейчас последний рейс в ${
                            lastTime.format(
                                DateTimeFormatter.ofPattern("HH:mm")
                            )
                        })!"
                    )
                }
                return false
            } else {
                mainScope.launch {
                    showError(
                        "Время начала последнего рейса - ${lastTime.format(DateTimeFormatter.ofPattern("HH:mm"))}\nВремя конца последнего рейса - ${
                            lastTime.addDouble(routeTime).format(
                                DateTimeFormatter.ofPattern("HH:mm")
                            )
                        }"
                    )
                }
                return true
            }
        }catch (e: kotlin.Exception){
            showError("Произошла ошибка при обращении на сервер. Попробуйте позже!")
            return false
        }
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
        mainScope.launch {
            errorText.text = text
            errorText.isVisible = false
            delay(100)
            errorText.isVisible = true
        }
    }

    private fun LocalTime.addDouble(time: Double): LocalTime {
        val hours = time.toLong()
        val minutes = (time - hours) * 60
        return this.plusHours(hours).plusMinutes(minutes.toLong())
    }

    private fun LocalDateTime.addDouble(time: Double): LocalDateTime {
        val hours = time.toLong()
        val minutes = (time - hours) * 60
        return this.plusHours(hours).plusMinutes(minutes.toLong())
    }
}