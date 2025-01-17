package com.example.scheschedule.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// 홈 화면 UI를 정의하는 Composable 함수
@Composable
fun HomeScreen() {
    // Box: 단일 레이아웃 컨테이너로, 자식 view를 배치하고 스타일링할 수 있음
    Box(
        modifier = Modifier.fillMaxSize(), // Box가 화면 전체를 채우도록 설정
        contentAlignment = Alignment.Center // 자식 view를 박스 중앙에 정렬
    ) {
        // 중앙에 텍스트 표시
        Text(text = "Home Screen") // 화면 중앙에 "Home Screen" 텍스트 출력
    }
}
