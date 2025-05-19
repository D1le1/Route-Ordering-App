package com.D1le

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import java.lang.Exception


class Tests {
    private val handler = DbHandler()
    @Before
    fun connect(){
        try{
            handler.connectToDb()
            assertNotNull(handler.getAuth("111", "f6e0a1e2ac41945a9aa7ff8a8aaa0cebc12a3bcc981a929ad5cf810a090e11ae"))
        }catch (_: Exception){
            assertNotNull(handler.getAuth("111", "f6e0a1e2ac41945a9aa7ff8a8aaa0cebc12a3bcc981a929ad5cf810a090e11ae"))
        }
    }

    @Test
    fun getAuth(){
        assertNotNull(handler.getAuth("111", "f6e0a1e2ac41945a9aa7ff8a8aaa0cebc12a3bcc981a929ad5cf810a090e11ae"))
    }

    @Test
    fun getTrips(){
        assertNotNull(handler.getHistoryTrips(1))
    }

    @Test
    fun confirmOrder(){
        assertTrue(handler.confirmOrder(7, 1, "Улица Гоголя"))
    }

    @Test
    fun getSerach(){
        assertTrue(handler.getSearchTrips("Минск", "Бобруйск", "25.05.2023").isNotEmpty())
    }
}