package com.example.scheschedule.ui.notice

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scheschedule.ui.viewmodel.NoticeViewModel
import com.example.scheschedule.ui.viewmodel.hasRecentNotices

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScholarshipNoticeScreen(viewModel: NoticeViewModel = viewModel()) {
    val notices by viewModel.scholarshipNotices.collectAsState()
    val error by viewModel.scholarshipError.collectAsState()
    // 3일 이내 게시글 존재 여부 확인
    val isRecentExists = hasRecentNotices(notices)

    NoticeListScreen(
        title = "장학 공지사항",
        notices = notices,
        error = error,
        isRecentExists,
        viewModel = viewModel,
        type = "scholarship"
    )
}