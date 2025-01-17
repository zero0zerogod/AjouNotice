package com.example.scheschedule.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scheschedule.ui.HomeScreen
import com.example.scheschedule.ui.NoticeScreen
import com.example.scheschedule.ui.ScheduleScreen
import com.example.scheschedule.ui.SettingsScreen

// NavGraph: 네비게이션 그래프 정의
@Composable
fun NavGraph(navController: NavHostController) {
    // NavHost: 네비게이션 호스트를 정의하며, 다양한 경로(route)와 이를 처리할 화면을 매핑
    NavHost(
        navController = navController, // 네비게이션 컨트롤러
        startDestination = "home" // 초기 경로를 "home"으로 설정
    ) {
        // "home" 경로에 대해 HomeScreen composable과 연결
        composable("home") {
            HomeScreen() // 홈 화면을 표시
        }
        // "schedule" 경로에 대해 " "
        composable("schedule") {
            ScheduleScreen() // 시간표 화면을 표시
        }
        // "notification" 경로에 대해 " "
        composable("notice") {
            NoticeScreen() // 공지사항 화면을 표시
        }
        // "settings" 경로에 대해 " "
        composable("settings") {
            SettingsScreen() // 설정 화면을 표시
        }
    }
}
