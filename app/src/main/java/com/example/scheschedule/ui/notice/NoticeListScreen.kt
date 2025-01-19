package com.example.scheschedule.ui.notice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scheschedule.components.NoticeRow
import com.example.scheschedule.model.Notice

@Composable
fun NoticeListScreen(
    title: String, notices: List<Notice>, error: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        if (error != null) {
            // 에러 메시지 표시
            Text(
                text = "에러 발생: $error",
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            // 공지사항 리스트 표시
            notices.forEach { notice ->
                NoticeRow(notice)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp), thickness = 1.dp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

