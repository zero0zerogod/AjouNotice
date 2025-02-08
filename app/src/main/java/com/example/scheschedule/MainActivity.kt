package com.example.scheschedule

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.scheschedule.components.Sidebar
import com.example.scheschedule.navigation.NavGraph
import com.example.scheschedule.ui.theme.ScheScheduleTheme
import com.example.scheschedule.viewmodel.DeveloperViewModel
import com.example.scheschedule.viewmodel.NoticeViewModel
import com.example.scheschedule.viewmodel.PatchNotesViewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 100
        private const val TAG = "MainActivity"
    }

    private val viewModel: NoticeViewModel by viewModels()
    private val developerViewModel: DeveloperViewModel by viewModels()
    private val patchNotesViewModel: PatchNotesViewModel by viewModels() // ✅ PatchNotesViewModel 추가

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 초기 데이터 로드
        viewModel.fetchAllNotices()

        // FCM 알림에서 전달된 noticeType 처리
        val initialType = intent?.getStringExtra("noticeType") ?: "home"
        viewModel.updateNoticeType(initialType)
        developerViewModel.fetchDeveloperInfo() // 🔥 앱 실행과 동시에 Firestore 데이터 미리 불러오기
        patchNotesViewModel.fetchPatchNotes() // 앱 실행과 동시에 Firestore 데이터 불러옴

        // BroadcastReceiver 등록
        viewModel.registerBroadcastReceiver(this)

        // Android 13 이상 알림 권한 요청
        requestNotificationPermissionIfNeeded()

        // FCM 토픽 구독 (서버에서 /topics/notices 로 보낼 때 수신 가능)
        subscribeToNoticesTopic()

        enableEdgeToEdge() // 전체 화면을 사용하는 레이아웃을 활성화
        setContent {
            ScheScheduleTheme {
                val navController = rememberNavController()
                val currentTypeState = viewModel.currentType.collectAsState()

                // Compose에 전달할 현재 noticeType
                val currentType = currentTypeState.value
                Log.d(TAG, "onCreate: 알림 타입 - $currentTypeState")

                MainScreen(
                    navController = navController,
                    currentType = currentType,
                    developerViewModel = developerViewModel,
                    patchNotesViewModel = patchNotesViewModel // ✅ 전달
                )
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // 🔥 [수정됨] 기존 Intent를 새 Intent로 업데이트하여 올바른 데이터가 반영되도록 함
        setIntent(intent)

        // 알림 클릭 등으로 새 Intent가 들어왔을 때
        val newType = intent.getStringExtra("noticeType") ?: "home"
        Log.d(TAG, "onNewIntent: 새로운 알림 타입 - $newType")

        // ViewModel 상태 업데이트
        viewModel.updateNoticeType(newType)
    }

    override fun onDestroy() {
        super.onDestroy()
        // BroadcastReceiver 해제
        viewModel.unregisterBroadcastReceiver(this)
    }

    /**
     * Android 13(API 33) 이상에서 알림 권한 요청
     */
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck =
                ContextCompat.checkSelfPermission(/* context = */ this,/* permission = */
                    Manifest.permission.POST_NOTIFICATIONS
                )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(/* activity = */ this,/* permissions = */
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),/* requestCode = */
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    /**
     * FCM 토픽("notices")에 구독
     */
    private fun subscribeToNoticesTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("notices").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Successfully subscribed to topic: notices")
            } else {
                println("Failed to subscribe to topic: notices ${task.exception}")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    currentType: String,
    developerViewModel: DeveloperViewModel,
    patchNotesViewModel: PatchNotesViewModel
) {
    // 사이드바의 초기 상태 설정 (닫힌 상태)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope() // 코루틴 스코프 생성

    // 알림 타입에 따른 네비게이션
    LaunchedEffect(currentType) {
        val destination = when (currentType) {
            "general" -> "notice/general"
            "scholarship" -> "notice/scholarship"
            "dormitory" -> "notice/dormitory"
            "department_ece" -> "notice/department_ece"
            "department_aisemi" -> "notice/department_aisemi"
            else -> "home"
        }

        val currentRoute = navController.currentBackStackEntry?.destination?.route
        Log.d("MainScreen", "현재 경로: $currentRoute, 이동할 경로: $destination")

        // 이미 같은 라우트면 navigate를 호출하지 않아도 됨
        if (currentRoute != destination) {
            navController.navigate(destination) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    ModalNavigationDrawer(drawerState = drawerState, // 사이드바 상태
        drawerContent = {
            Sidebar(navController = navController, drawerState = drawerState) // 사이드바 composable
        }) {
        Scaffold(topBar = {
            TopAppBar(title = { Text("ScheSchedule") }, // App Bar 제목 설정
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open() // 메뉴 버튼 클릭 시 사이드바 열기
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu") // 햄버거 메뉴 아이콘
                    }
                })
        }, content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Log.d("MainScreen", "Scaffold: NavGraph 설정 시작")
                NavGraph(
                    navController = navController,
                    developerViewModel = developerViewModel,
                    patchNotesViewModel = patchNotesViewModel
                )
            }
        })
    }
}
