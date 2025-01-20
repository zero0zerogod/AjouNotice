package com.example.scheschedule.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheschedule.model.Notice
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoticeRow(notice: Notice) {
    val uriHandler = LocalUriHandler.current // URI 처리기

    // 날짜 비교 로직
    val isRecent = LocalDate.now().minusDays(3).isBefore(LocalDate.parse(notice.date))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { uriHandler.openUri(notice.link) } // 클릭 시 링크 열기
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        // 제목
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = notice.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 하위 정보 (카테고리, 작성 부서, 날짜)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${notice.category} | ",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = notice.department,
                fontSize = 14.sp,
                color = Color.Gray
            )

            if (isRecent) {
                Text(
                    text = "new",
                    fontSize = 12.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // 가로 공간 확보
            Spacer(modifier = Modifier.weight(1f))

            // 오른쪽 정렬 텍스트 (날짜)
            Text(
                text = notice.date,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
