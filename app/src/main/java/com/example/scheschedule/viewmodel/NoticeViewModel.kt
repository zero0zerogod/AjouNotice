package com.example.scheschedule.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.scheschedule.model.Notice
import com.example.scheschedule.repository.NoticeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class NoticeViewModel : ViewModel() {
    private val repository = NoticeRepository() // NoticeRepository 객체 생성
    private var receiver: BroadcastReceiver? = null// BroadcastReceiver 멤버 변수 추가

    // 현재 선택된 타입을 관리하는 StateFlow
    private val _currentType = MutableStateFlow("home")
    val currentType: StateFlow<String> get() = _currentType

    // 각각의 공지사항 데이터를 담는 상태 플로우 및 오류 상태
    private val noticeStateMap = mutableMapOf(
        "general" to NoticeState(),
        "scholarship" to NoticeState(),
        "dormitory" to NoticeState(),
        "department_ece" to NoticeState(),
        "department_aisemi" to NoticeState()
    )

    // getter: 외부에서 StateFlow로 접근
    val generalNotices: StateFlow<List<Notice>> get() = noticeStateMap["general"]!!.notices
    val generalError: StateFlow<String?> get() = noticeStateMap["general"]!!.error
    val scholarshipNotices: StateFlow<List<Notice>> get() = noticeStateMap["scholarship"]!!.notices
    val scholarshipError: StateFlow<String?> get() = noticeStateMap["scholarship"]!!.error
    val dormitoryNotices: StateFlow<List<Notice>> get() = noticeStateMap["dormitory"]!!.notices
    val dormitoryError: StateFlow<String?> get() = noticeStateMap["dormitory"]!!.error
    val eceNotices: StateFlow<List<Notice>> get() = noticeStateMap["department_ece"]!!.notices
    val eceError: StateFlow<String?> get() = noticeStateMap["department_ece"]!!.error
    val aiSemiNotices: StateFlow<List<Notice>> get() = noticeStateMap["department_aisemi"]!!.notices
    val aiSemiError: StateFlow<String?> get() = noticeStateMap["department_aisemi"]!!.error

    init {
        fetchAllNotices()
    }

    fun fetchAllNotices() {
        noticeStateMap.keys.forEach { type ->
            refreshNotices(type) {}
        }
    }

    fun refreshNotices(type: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val state = noticeStateMap[type] ?: return@launch
            try {
                val newNotices = fetchNoticesByType(type)
                val hasChanges = newNotices != state.notices.value
                if (hasChanges) {
                    state.notices.value = newNotices
                }
                onComplete(hasChanges)
            } catch (e: Exception) {
                state.error.value = e.message
                onComplete(false)
            }
        }
    }

    private suspend fun fetchNoticesByType(type: String): List<Notice> {
        return when (type) {
            "general" -> repository.getGeneralNotices()
            "scholarship" -> repository.getScholarshipNotices()
            "dormitory" -> repository.getDormitoryNotices()
            "department_ece" -> repository.getECENotices()
            "department_aisemi" -> repository.getAiSemiNotices()
            else -> emptyList()
        }
    }

    fun updateNoticeType(type: String) {
        _currentType.value = type
        Log.d("NoticeViewModel", "Notice type updated to: $type")
    }

    // LocalBroadcastManager 등록 및 해제
    fun registerBroadcastReceiver(context: Context) {
        receiver = object : BroadcastReceiver() { // 클래스 멤버 변수 receiver에 할당
            override fun onReceive(context: Context, intent: Intent) {
                val type = intent.getStringExtra("noticeType") ?: return
                Log.d("NoticeViewModel", "BroadcastReceiver 수신 - type: $type")
                refreshNotices(type) {}
                updateNoticeType(type) // ViewModel의 타입 업데이트
            }
        }
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(receiver!!, IntentFilter("ACTION_REFRESH_NOTICES"))
        Log.d("NoticeViewModel", "BroadcastReceiver 등록 완료")
    }

    fun unregisterBroadcastReceiver(context: Context) {
        receiver?.let {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(it)
            Log.d("NoticeViewModel", "BroadcastReceiver 해제 완료")
        }
    }

    private class NoticeState(
        val notices: MutableStateFlow<List<Notice>> = MutableStateFlow(emptyList()),
        val error: MutableStateFlow<String?> = MutableStateFlow(null)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun hasRecentNotices(notices: List<Notice>): Boolean {
    return notices.any { notice ->
        val postDate = LocalDate.parse(notice.date) // notice.date가 yyyy-MM-dd 형식이라고 가정
        postDate.isAfter(LocalDate.now().minusDays(3))
    }
}
