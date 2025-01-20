package com.example.scheschedule.ui.notice

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scheschedule.components.NoticeRow
import com.example.scheschedule.model.Notice

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoticeListScreen(
    title: String, notices: List<Notice>, error: String?, isRecentExists: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 제목
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            // 만약 최근 공지가 있다면 "new" 표시
            if (isRecentExists) {
                Text(
                    text = "new", color = Color.Red, modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
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
