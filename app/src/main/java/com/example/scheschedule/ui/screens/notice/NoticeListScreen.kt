package com.example.scheschedule.ui.screens.notice

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
    // Snackbar & PullRefresh
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

    // 고정 공지사항 토글, 페이징, 스크롤
    var fixedExpanded by remember { mutableStateOf(true) }
    var currentPage by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()

    // 페이지 변경 시 스크롤 최상단 이동
    LaunchedEffect(currentPage) {
        scrollState.animateScrollTo(0)
    }

    // 고정 vs 일반 공지
    val fixedNotices = notices.filter { it.number == "공지" }
    val generalNotices = notices.filter { it.number != "공지" }

    // 일반 공지 페이징(한 페이지 10개)
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
            // 화면 제목 + new 표시
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

            // 에러 메시지 표시
            if (error != null) {
                Text(
                    text = "에러 발생: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 고정 공지사항 영역 (헤더 + 행들까지 전체 배경)
            if (fixedExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE0E0E0))  // 라이트 그레이
                        .padding(8.dp)
                ) {
                    // 고정 공지사항 헤더(색 포함)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { fixedExpanded = !fixedExpanded }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "고정 공지사항",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "닫기",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }

                    // 고정 공지사항 각 행
                    fixedNotices.forEachIndexed { i, notice ->
                        NoticeRow(notice)
                        // 행 사이에만 구분선, 마지막 행 아래는 공백 최소화
                        if (i < fixedNotices.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                thickness = 1.dp,
                                color = Color.LightGray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                // 닫힌 상태 헤더 (색 없음)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { fixedExpanded = !fixedExpanded }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "고정 공지사항",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "열기",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
                // 닫힌 상태 → 라이트 그레이 없이 바로 아래 일반 공지로
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 일반 공지사항
            currentGeneralNotices.forEachIndexed { i, notice ->
                NoticeRow(notice)
                // 행 사이에만 구분선
                if (i < currentGeneralNotices.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 1.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 페이징 영역
            if (generalNotices.isNotEmpty()) {
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
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // PullRefresh
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        )

        // Snackbar
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            Snackbar(snackbarData = data)
        }
    }
}
