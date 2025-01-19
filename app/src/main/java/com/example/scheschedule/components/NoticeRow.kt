package com.example.scheschedule.components

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

@Composable
fun NoticeRow(notice: Notice) {
    val uriHandler = LocalUriHandler.current // URI 처리기

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { uriHandler.openUri(notice.link) } // 클릭 시 링크 열기
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        // 제목
        Text(
            text = notice.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

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
            // 가로 공간 확보
            Spacer(modifier = Modifier.weight(1f))

            // 오른쪽 정렬 텍스트 (날짜)
            Text(
                text = notice.date,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
