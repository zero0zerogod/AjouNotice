package com.example.scheschedule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheschedule.model.Notice
import com.example.scheschedule.repository.NoticeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoticeViewModel : ViewModel() {
    private val repository = NoticeRepository() // NoticeRepository 객체 생성

    // 각각의 공지사항 데이터를 담는 상태 플로우 / API 요청 중 발생한 오류
    // 일반 공지사항
    private val _generalNotices = MutableStateFlow<List<Notice>>(emptyList())
    val generalNotices: StateFlow<List<Notice>> = _generalNotices

    private val _generalError = MutableStateFlow<String?>(null)
    val generalError: StateFlow<String?> = _generalError
// --------------------------------------------------------------------------------
    // 장학 공지사항
    private val _scholarshipNotices = MutableStateFlow<List<Notice>>(emptyList())
    val scholarshipNotices: StateFlow<List<Notice>> = _scholarshipNotices

    private val _scholarshipError = MutableStateFlow<String?>(null)
    val scholarshipError: StateFlow<String?> = _scholarshipError
// --------------------------------------------------------------------------------
    // 생활관 공지사항
    private val _dormitoryNotices = MutableStateFlow<List<Notice>>(emptyList())
    val dormitoryNotices: StateFlow<List<Notice>> = _dormitoryNotices

    private val _dormitoryError = MutableStateFlow<String?>(null)
    val dormitoryError: StateFlow<String?> = _dormitoryError
// --------------------------------------------------------------------------------
    // 전자공학과 공지사항
    private val _eceNotices = MutableStateFlow<List<Notice>>(emptyList())
    val eceNotices: StateFlow<List<Notice>> = _eceNotices

    private val _eceError = MutableStateFlow<String?>(null)
    val eceError: StateFlow<String?> = _eceError
// --------------------------------------------------------------------------------
    // 지능형반도체공학과 공지사항
    private val _aiSemiNotices = MutableStateFlow<List<Notice>>(emptyList())
    val aiSemiNotices: StateFlow<List<Notice>> = _aiSemiNotices

    private val _aiSemiError = MutableStateFlow<String?>(null)
    val aiSemiError: StateFlow<String?> = _aiSemiError


    // 초기화 시 데이터 가져오기
    init {
        fetchNotices()
    }

    // 공지사항 데이터를 가져오는 메서드
    private fun fetchNotices() {
        viewModelScope.launch {
            // 일반 공지사항
            try {
                _generalNotices.value = repository.getGeneralNotices()
            } catch (e: Exception) {
                _generalError.value = e.message
            }

            // 장학 공지사항
            try {
                _scholarshipNotices.value = repository.getScholarshipNotices()
            } catch (e: Exception) {
                _scholarshipError.value = e.message
            }

            // 생활관 공지사항
            try {
                _dormitoryNotices.value = repository.getDormitoryNotices()
            } catch (e: Exception) {
                _dormitoryError.value = e.message
            }

            // 전자공학과 공지사항
            try {
                _eceNotices.value = repository.getECENotices()
            } catch (e: Exception) {
                _eceError.value = e.message
            }

            // 지능형반도체공학과 공지사항
            try {
                _aiSemiNotices.value = repository.getAiSemiNotices()
            } catch (e: Exception) {
                _aiSemiError.value = e.message
            }
        }
    }
}