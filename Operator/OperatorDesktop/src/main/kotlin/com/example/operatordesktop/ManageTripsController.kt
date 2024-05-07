package com.example.operatordesktop

import com.example.operatordesktop.util.ServerWork
import com.example.operatordesktop.util.Trip
import javafx.scene.control.ListView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageTripsController {
    lateinit var tripsList:ListView<Trip>

    fun initialize(){
        CoroutineScope(Dispatchers.IO).launch {
            ServerWork.sendRequest("TRIPS")
        }
    }
}