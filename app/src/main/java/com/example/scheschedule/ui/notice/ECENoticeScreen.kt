package com.example.scheschedule.ui.notice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scheschedule.ui.viewmodel.NoticeViewModel

@Composable
fun ECENoticeScreen(viewModel: NoticeViewModel = viewModel()) {
    val notices by viewModel.eceNotices.collectAsState()
    val error by viewModel.eceError.collectAsState()
    NoticeListScreen(title = "전자공학과 공지사항", notices = notices, error = error)
}