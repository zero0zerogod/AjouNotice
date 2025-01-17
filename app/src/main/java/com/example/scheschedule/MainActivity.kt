package com.example.scheschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.scheschedule.components.Sidebar
import com.example.scheschedule.navigation.NavGraph
import com.example.scheschedule.ui.theme.ScheScheduleTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 전체 화면을 사용하는 레이아웃을 활성화
        setContent {
            ScheScheduleTheme {
                MainScreen() // 메인 화면 렌더링
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController() // 네비게이션 컨트롤러 생성
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) // 사이드바의 초기 상태 설정 (닫힌 상태)
    val scope = rememberCoroutineScope() // 코루틴 스코프 생성

    ModalNavigationDrawer(
        drawerState = drawerState, // 사이드바 상태
        drawerContent = {
            Sidebar(navController = navController, drawerState = drawerState) // 사이드바 composable
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("ScheSchedule") }, // App Bar 제목 설정
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open() // 메뉴 버튼 클릭 시 사이드바 열기
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu") // 햄버거 메뉴 아이콘
                        }
                    }
                )
            },
            content = { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavGraph(navController = navController) // 네비게이션 그래프 추가
                }
            }
        )
    }
}
