package com.example.scheschedule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 🔥 Firestore에서 불러올 데이터 모델
data class DeveloperInfo(
    val name: String = "",
    val department: String = "",
    val grade: String = "",
    val role: String = "",
    val email: String = ""
)

class DeveloperViewModel : ViewModel() {
    private val _developerInfo = MutableStateFlow(DeveloperInfo())  // 🔥 Flow로 상태 관리
    val developerInfo: StateFlow<DeveloperInfo> get() = _developerInfo

    init {
        fetchDeveloperInfo()
    }

    fun fetchDeveloperInfo() {
        val db = FirebaseFirestore.getInstance()
        db.collection("developer_info").document("profile").get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val info = document.toObject(DeveloperInfo::class.java)
                    info?.let {
                        viewModelScope.launch {
                            _developerInfo.emit(it) // 🔥 Firestore에서 가져온 데이터 업데이트
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace() // 오류 로깅
            }
    }
}
