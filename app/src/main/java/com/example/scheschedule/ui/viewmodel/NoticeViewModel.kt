package com.example.scheschedule.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheschedule.model.Notice
import com.example.scheschedule.repository.NoticeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

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
        fetchAllNotices()
    }

    // 1) 앱 실행하면 모든 공지사항을 불러옴
    fun fetchAllNotices() {
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

    // 2) 알림 클릭 or PullToRefresh 시 “일반 공지”만 다시 로드
    fun refreshNotices(type: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val newNotices = when (type) {
                    "general" -> repository.getGeneralNotices()
                    "scholarship" -> repository.getScholarshipNotices()
                    "dormitory" -> repository.getDormitoryNotices()
                    "department_ece" -> repository.getECENotices()
                    "department_aisemi" -> repository.getAiSemiNotices()
                    else -> emptyList()
                }
                // 변경사항 확인
                val hasChanges = when (type) {
                    "general" -> newNotices != _generalNotices.value
                    "scholarship" -> newNotices != _scholarshipNotices.value
                    "dormitory" -> newNotices != _dormitoryNotices.value
                    "department_ece" -> newNotices != _eceNotices.value
                    "department_aisemi" -> newNotices != _aiSemiNotices.value
                    else -> false
                }
                // 변경사항이 있으면 상태 갱신
                if (hasChanges) {
                    when (type) {
                        "general" -> _generalNotices.value = newNotices
                        "scholarship" -> _scholarshipNotices.value = newNotices
                        "dormitory" -> _dormitoryNotices.value = newNotices
                        "department_ece" -> _eceNotices.value = newNotices
                        "department_aisemi" -> _aiSemiNotices.value = newNotices
                    }
                }
                onComplete(hasChanges) // 로드 완료 후 호출
            } catch (e: Exception) {
                onComplete(false) // 에러 발생 시에도 호출
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun hasRecentNotices(notices: List<Notice>): Boolean {
    return notices.any { notice ->
        val postDate = LocalDate.parse(notice.date) // notice.date가 yyyy-MM-dd 형식이라고 가정
        postDate.isAfter(LocalDate.now().minusDays(3))
    }
}