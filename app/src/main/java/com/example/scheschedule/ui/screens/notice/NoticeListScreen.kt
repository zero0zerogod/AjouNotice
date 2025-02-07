package com.example.scheschedule.ui.screens.notice

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheschedule.components.NoticeRow
import com.example.scheschedule.model.Notice
import com.example.scheschedule.viewmodel.NoticeViewModel
import kotlinx.coroutines.launch
import kotlin.math.ceil

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoticeListScreen(
    title: String,
    notices: List<Notice>,
    error: String?,
    isRecentExists: Boolean = false,
    viewModel: NoticeViewModel,
    type: String
) {
    // ✅ Snackbar & PullRefresh
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.refreshNotices(type) { hasChanges ->
                isRefreshing = false
                if (!hasChanges) {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = "변경된 공지사항이 없습니다.",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    )

    // ✅ 고정 공지사항 토글, 페이징, 스크롤
    var fixedExpanded by remember { mutableStateOf(true) }
    var currentPage by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()

    // ✅ 페이지 변경 시 스크롤 최상단 이동
    LaunchedEffect(currentPage) {
        scrollState.animateScrollTo(0)
    }

    // ✅ 고정 vs 일반 공지
    val fixedNotices = notices.filter { it.number == "공지" }
    val generalNotices = notices.filter { it.number != "공지" }

    // ✅ 일반 공지 페이징(한 페이지 10개)
    val totalPages = if (generalNotices.isEmpty()) 1 else ceil(generalNotices.size / 10.0).toInt()
    val startIndex = (currentPage - 1) * 10
    val currentGeneralNotices = generalNotices.drop(startIndex).take(10)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // ✅ 화면 제목 + new 표시
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                if (isRecentExists) {
                    Text(
                        text = "new",
                        color = Color.Red,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // ✅ 에러 메시지 표시
            error?.let {
                Text(
                    text = "에러 발생: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ 고정 공지사항 영역 (카드 스타일)
            if (fixedNotices.isNotEmpty()) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { fixedExpanded = !fixedExpanded }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📌 고정 공지사항",
                                color = MaterialTheme.colorScheme.onPrimaryContainer, // ✅ 대비 최적화된 텍스트 색상 적용
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (fixedExpanded) "▲ 닫기" else "▼ 열기",
                                color = MaterialTheme.colorScheme.onPrimaryContainer, // ✅ 대비 색상 자동 조정
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                        if (fixedExpanded) {
                            fixedNotices.forEach { notice ->
                                NoticeRow(notice)
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ✅ 일반 공지사항 리스트
            currentGeneralNotices.forEach { notice ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest), // 🔥 일반 공지사항은 밝게
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    NoticeRow(notice)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 기존 스타일의 페이징 UI 유지 (단순한 텍스트 UI)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (currentPage > 1) {
                    Text(
                        text = "◀",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { currentPage-- },
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                for (page in 1..totalPages) {
                    Text(
                        text = page.toString(),
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { currentPage = page },
                        fontSize = 16.sp,
                        fontWeight = if (page == currentPage) FontWeight.Bold else FontWeight.Normal,
                        color = if (page == currentPage) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
                if (currentPage < totalPages) {
                    Text(
                        text = "▶",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { currentPage++ },
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}
