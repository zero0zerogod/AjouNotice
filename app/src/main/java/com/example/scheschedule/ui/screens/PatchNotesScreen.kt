package com.example.scheschedule.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scheschedule.viewmodel.PatchNote
import com.example.scheschedule.viewmodel.PatchNotesViewModel

@Composable
fun PatchNotesScreen(patchNotesViewModel: PatchNotesViewModel) {
    val patchNotes by patchNotesViewModel.patchNotes.collectAsState()
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() } // 버전별 확장 여부 저장

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(patchNotes) { note ->
            PatchNoteItem(note, expandedStates)
        }
    }
}

@Composable
fun PatchNoteItem(note: PatchNote, expandedStates: MutableMap<String, Boolean>) {
    var isExpanded by remember { mutableStateOf(expandedStates[note.version] ?: false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // ✅ 패치노트 버튼 (Primary 색상 적용)
        Card(modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.elevatedCardElevation(4.dp),
            onClick = {
                isExpanded = !isExpanded
                expandedStates[note.version] = isExpanded // 상태 저장
            }) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "version ${note.version} (${note.date})",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimary // ✅ 대비되는 텍스트 색상
                )
            }
        }

        if (isExpanded) {
            // ✅ 패치노트 내용 카드 (PrimaryContainer 색상 적용)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.elevatedCardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    note.changes.forEach { change ->
                        Text(
                            text = "• $change",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer // ✅ 대비되는 텍스트 색상
                        )
                    }
                }
            }
        }
    }
}
