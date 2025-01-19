package com.example.scheschedule.ui.notice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scheschedule.ui.viewmodel.NoticeViewModel

@Composable
fun DormitoryNoticeScreen(viewModel: NoticeViewModel = viewModel()) {
    val notices by viewModel.dormitoryNotices.collectAsState()
    val error by viewModel.dormitoryError.collectAsState()

    NoticeListScreen(
        title = "생활관 공지사항", notices = notices, error = error
    )
}
