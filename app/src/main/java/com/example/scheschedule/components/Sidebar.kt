package com.example.scheschedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scheschedule.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// 사이드바 안의 개별 버튼 구성
@Composable
fun SidebarButton(
    label: String, // 버튼 텍스트
    navController: NavController, // 네비게이션 컨트롤러
    route: String, // 이동할 네이게이션 경로
    drawerState: DrawerState, // DrawerState 객체
    scope: CoroutineScope, // 코루틴 스코프
    icon: @Composable (() -> Unit)? = null // 아이콘 Composable을 선택적으로 전달
) {
    Button(
        onClick = {
            // 버튼 클릭 시 동작
            scope.launch {
                drawerState.close() // 버튼 클릭 시 사이드바를 닫음
            }
            navController.navigate(route) // 지정된 네비게이션 경로로 이동
        }, modifier = Modifier
            .fillMaxWidth() // 버튼이 사이드바의 가로를 완전히 채우도록 설정
            .height(48.dp), // 버튼의 높이를 고정하여 균일한 크기 유지
        shape = Shapes().small.copy(CornerSize(0.dp)) // 버튼 모서리를 직각으로 설정
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), // 아이콘과 텍스트를 가로로 배치
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically // 세로 중앙 정렬
        ) {
            if (icon != null) {
                Box(modifier = Modifier.padding(end = 8.dp)) { // 텍스트와의 간격 설정
                    icon() // 아이콘 표시
                }
            }
            Text(text = label) // 버튼에 텍스트 표시
        }
    }
}

@Composable
fun SubSidebarButton(
    label: String,
    navController: NavController,
    route: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    paddingStart: Int = 32, // 기본값
    icon: @Composable (() -> Unit)? = null // 아이콘을 선택적으로 추가
) {
    Button(
        onClick = {
            // 버튼 클릭 시 동작
            scope.launch {
                drawerState.close() // 버튼 클릭 시 사이드바를 닫음
            }
            navController.navigate(route) // 지정된 네비게이션 경로로 이동
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(start = 0.dp),
        shape = Shapes().small.copy(CornerSize(0.dp)) // 버튼 모서리를 직각으로 설정
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = paddingStart.dp), // 전체 Row에 paddingStart 적용

            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically // 세로 중앙 정렬
        ) {
            if (icon != null) {
                Box(modifier = Modifier.padding(end = 8.dp)) { icon() }
            }
            Text(text = label) // 버튼에 텍스트 표시
        }
    }
}


// Sidebar: 사이드바 구성
@Composable
fun Sidebar(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope() // 코루틴을 사용하여 비동기 동작 제어
    val isNoticeExpanded = remember { mutableStateOf(false) } // 공지사항 열림 상태 관리
    val isDepartmentExpanded = remember { mutableStateOf(false) } // 학과 공지사항 열림 상태 관리

    // 사이드바 닫힘 시 공지사항 접기
    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            isNoticeExpanded.value = false
            isDepartmentExpanded.value = false // 닫힐 때 학과 공지사항도 초기화
        }
    }

    // 사이드바의 전체 레이아웃
    Box(
        modifier = Modifier
            .fillMaxHeight() // 화면의 세로를 모두 채우도록 설정
            .width(270.dp) // 사이드바의 가로 크기를 270dp로 고정
            .background(Color.LightGray) // 사이드바의 배경색을 연회색으로 설정
            .padding(vertical = 16.dp) // 내부 수직 패딩 추가
    ) {
        // 버튼을 세로로 배치하기 위한 Column
        Column(
            modifier = Modifier.padding(vertical = 16.dp) // 버튼 간 여유 공간
        ) {
            // 각각의 버튼을 호출하여 사이드바에 추가
            SidebarButton("홈", navController, "home", drawerState, scope) {
                Icon(Icons.Default.Home, contentDescription = "Home Icon") // 아이콘 추가
            }
            SidebarButton("시간표", navController, "schedule", drawerState, scope) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Home Icon")
            }

            // 공지사항 버튼
            Button(
                onClick = { isNoticeExpanded.value = !isNoticeExpanded.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 0.dp), // 왼쪽 정렬 유지
                shape = Shapes().small.copy(CornerSize(0.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notice Icon")
                    Text(
                        text = "공지사항", modifier = Modifier.padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f)) // 텍스트와 상태 아이콘 사이 간격 추가
                    Icon(
                        if (isNoticeExpanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand Icon"
                    )
                }
            }
            // 사용 예시
            if (isNoticeExpanded.value) {
                SubSidebarButton("일반 공지사항",
                    navController,
                    "notice/general",
                    drawerState,
                    scope,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.document),
                            contentDescription = "General Icon",
                            modifier = Modifier
                                .size(22.dp)
                                .padding(start=1.dp, end = 1.dp)
                        )
                    })
                SubSidebarButton("장학 공지사항",
                    navController,
                    "notice/scholarship",
                    drawerState,
                    scope,
                    icon = {
                        Icon(Icons.Default.School, contentDescription = "Scholarship Icon")
                    })
                SubSidebarButton("생활관 공지사항",
                    navController,
                    "notice/dormitory",
                    drawerState,
                    scope,
                    icon = {
                        Icon(Icons.Default.Hotel, contentDescription = "Dormitory Icon")
                    })

                // 학과 공지사항 버튼
                Button(
                    onClick = { isDepartmentExpanded.value = !isDepartmentExpanded.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(start = 0.dp), // 생활관 공지사항 아래에 배치
                    shape = Shapes().small.copy(CornerSize(0.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Book, contentDescription = "Department Icon")
                        Text(
                            text = "학과 공지사항", modifier = Modifier.padding(start = 8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f)) // 텍스트와 상태 아이콘 사이 간격 추가
                        Icon(
                            if (isDepartmentExpanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand Icon"
                        )
                    }
                }

                if (isDepartmentExpanded.value) {
                    // 전자공학과 버튼
                    SubSidebarButton(
                        "전자공학과",
                        navController,
                        "notice/department_ece",
                        drawerState,
                        scope,
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_circuit_board),
                                contentDescription = "ECE Icon",
                                modifier = Modifier.size(24.dp), // 아이콘 크기
                            )
                        },
                        paddingStart = 64
                    )
                    // 전자공학과 버튼
                    SubSidebarButton(
                        "지능형반도체공학과",
                        navController,
                        "notice/department_aisemi",
                        drawerState,
                        scope,
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.cpu),
                                contentDescription = "AI_SEMI Icon",
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        },
                        paddingStart = 64
                    )
                }
            }


            SidebarButton("설정", navController, "settings", drawerState, scope) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }

            SidebarButton("About", navController, "info", drawerState, scope) {
                Icon(Icons.Default.Info, contentDescription = "Information")
            }
        }
    }
}
