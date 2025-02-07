package com.example.scheschedule.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scheschedule.ui.screens.DeveloperScreen
import com.example.scheschedule.ui.screens.HomeScreen
import com.example.scheschedule.ui.screens.ServiceScreen
import com.example.scheschedule.ui.screens.SettingsScreen
import com.example.scheschedule.ui.screens.notice.AiSemiNoticeScreen
import com.example.scheschedule.ui.screens.notice.DormitoryNoticeScreen
import com.example.scheschedule.ui.screens.notice.ECENoticeScreen
import com.example.scheschedule.ui.screens.notice.GeneralNoticeScreen
import com.example.scheschedule.ui.screens.notice.ScholarshipNoticeScreen
import com.example.scheschedule.viewmodel.DeveloperViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController, developerViewModel: DeveloperViewModel) {
    NavHost(
        navController = navController, startDestination = "home"
    ) {
        composable("home") { HomeScreen() }
        composable("service") { ServiceScreen() }
        composable("settings") { SettingsScreen() }

        // 공지사항 종류별 라우트 추가
        composable("notice/general") { GeneralNoticeScreen() }
        composable("notice/scholarship") { ScholarshipNoticeScreen() }
        composable("notice/dormitory") { DormitoryNoticeScreen() }
        composable("notice/department_ece") { ECENoticeScreen() }
        composable("notice/department_aisemi") { AiSemiNoticeScreen() }

        composable("developer") { DeveloperScreen(developerViewModel = developerViewModel) }
    }
}
