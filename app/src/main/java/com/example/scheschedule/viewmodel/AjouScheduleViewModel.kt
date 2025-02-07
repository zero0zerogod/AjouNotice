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

    private val _isLoading = MutableStateFlow(true) // ✅ 로딩 상태 추가
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isError = MutableStateFlow(false) // ✅ 에러 상태 추가
    val isError: StateFlow<Boolean> get() = _isError

    init {
        loadSchedule(2025) // 기본 연도
    }

    fun loadSchedule(year: Int) {
        viewModelScope.launch {
            _isLoading.value = true // ✅ 로딩 시작
            _isError.value = false // 초기화

            try {
                val result = repository.getAjouScheduleHtml(year)
                _scheduleHtml.value = result
                _isLoading.value = false // ✅ 로딩 완료
            } catch (e: Exception) {
                _isError.value = true
                _isLoading.value = false
            }
        }
    }
}
