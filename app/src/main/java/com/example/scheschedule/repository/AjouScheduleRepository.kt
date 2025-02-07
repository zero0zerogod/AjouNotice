package com.example.scheschedule.repository

import com.example.scheschedule.network.RetrofitInstance

class AjouScheduleRepository {
    suspend fun getAjouScheduleHtml(year: Int): String {
        val response = RetrofitInstance.api.getScheduleHtml(year)
        return if (response.isSuccessful) {
            response.body()?.string() ?: "<p>No schedule data</p>"
        } else {
            "<p>Error fetching schedule for $year</p>"
        }
    }
}

