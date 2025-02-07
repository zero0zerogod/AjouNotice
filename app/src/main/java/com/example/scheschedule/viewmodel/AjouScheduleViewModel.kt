package com.example.scheschedule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheschedule.repository.AjouScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AjouScheduleViewModel : ViewModel() {
    private val repository = AjouScheduleRepository()

    private val _scheduleHtml = MutableStateFlow("<p>Loading...</p>")
    val scheduleHtml: StateFlow<String> get() = _scheduleHtml


    init {
        loadSchedule(2025) // 기본연도
    }

    fun loadSchedule(year: Int) {
        viewModelScope.launch {
            _scheduleHtml.value = repository.getAjouScheduleHtml(year)
        }
    }
}
