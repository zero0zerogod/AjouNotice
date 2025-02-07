package com.example.scheschedule.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ServiceScreen() {
    // URI Handler: 브라우저에서 링크 열기
    val uriHandler = LocalUriHandler.current

    // Screen UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp), // 카드 간 간격
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "서비스 바로가기",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 학사 서비스 카드
            ServiceCard(
                title = "학사 서비스 바로가기",
                description = "학사 정보 및 서비스 확인",
                onClick = { uriHandler.openUri("https://mhaksa.ajou.ac.kr:30443/index.html") }
            )

            // 정보통신대학 학과요람 카드
            ServiceCard(
                title = "정보통신대학 학과요람",
                description = "정보통신대학 학과별 커리큘럼 확인",
                onClick = { uriHandler.openUri("https://www.ajou.ac.kr/kr/bachelor/bulletin.do?mode=list&srSearchKey=&srCategoryId=80&srSearchVal=") }
            )
        }
    }
}

// 개별 카드 Composable
@Composable
fun ServiceCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick, // 클릭 시 동작
        modifier = Modifier
            .fillMaxWidth(0.9f) // 카드 너비
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) // 설명 텍스트 밝기 조정
                )
            )
        }
    }
}
