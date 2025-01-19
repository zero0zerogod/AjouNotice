package com.example.scheschedule.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scheschedule.ui.HomeScreen
import com.example.scheschedule.ui.ScheduleScreen
import com.example.scheschedule.ui.SettingsScreen
import com.example.scheschedule.ui.notice.AiSemiNoticeScreen
import com.example.scheschedule.ui.notice.ECENoticeScreen
import com.example.scheschedule.ui.notice.DormitoryNoticeScreen
import com.example.scheschedule.ui.notice.GeneralNoticeScreen
import com.example.scheschedule.ui.notice.ScholarshipNoticeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen() }
        composable("schedule") { ScheduleScreen() }
        composable("settings") { SettingsScreen() }

        // 공지사항 종류별 라우트 추가
        composable("notice/general") { GeneralNoticeScreen() }
        composable("notice/scholarship") { ScholarshipNoticeScreen() }
        composable("notice/dormitory") { DormitoryNoticeScreen() }
        composable("notice/department_ece") { ECENoticeScreen() }
        composable("notice/department_aisemi") { AiSemiNoticeScreen()}

    }
}
