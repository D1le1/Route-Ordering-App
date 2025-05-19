package com.example.busik

import com.example.busik.other.AuthActivity
import com.example.busik.other.ServerWork
import com.example.busik.servertasks.AuthTask
import org.junit.Before
import org.junit.Assert.*
import org.junit.Test

class Tests {
    @Test
    fun connect(){
        ServerWork.connectToServer()
        val response = ServerWork.sendRequest("AUTH--111--${AuthActivity.hashPassword("111")}")
        println(response)
        assertEquals("OK", response)
    }
}