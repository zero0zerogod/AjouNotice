package com.example.scheschedule.ui.screens.notice

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scheschedule.viewmodel.NoticeViewModel
import com.example.scheschedule.viewmodel.hasRecentNotices

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GeneralNoticeScreen(viewModel: NoticeViewModel = viewModel()) {
    val notices by viewModel.generalNotices.collectAsState()
    val error by viewModel.generalError.collectAsState()
    val isRecentExists = hasRecentNotices(notices)     // 3일 이내 게시글 존재 여부 확인

    NoticeListScreen(
        title = "일반 공지사항",
        notices = notices,
        error = error,
        isRecentExists = isRecentExists,
        viewModel = viewModel,
        type = "general"
    )
}
