package com.example.scheschedule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Firestore에서 불러올 패치노트 데이터 모델
data class PatchNote(
    val version: String = "", val date: String = "", val changes: List<String> = emptyList()
)

class PatchNotesViewModel : ViewModel() {
    private val _patchNotes = MutableStateFlow<List<PatchNote>>(emptyList())
    val patchNotes: StateFlow<List<PatchNote>> get() = _patchNotes

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchPatchNotes()
    }

    fun fetchPatchNotes() {
        db.collection("patch_notes").get().addOnSuccessListener { documents ->
                val notes = documents.map { doc ->
                    PatchNote(version = doc.id,  // 문서 ID를 버전으로 사용
                        date = doc.getString("date") ?: "",
                        changes = (doc.get("changes") as? List<*>)?.mapNotNull { it as? String }
                            ?: emptyList())
                }.sortedByDescending { it.version } // 최신 버전이 위에 오도록 정렬

                viewModelScope.launch {
                    _patchNotes.emit(notes)
                }
            }.addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
