package com.example.scheschedule.ui.notice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scheschedule.ui.viewmodel.NoticeViewModel

@Composable
fun AiSemiNoticeScreen(viewModel: NoticeViewModel = viewModel()) {
    val notices by viewModel.aiSemiNotices.collectAsState()
    val error by viewModel.aiSemiError.collectAsState()
    NoticeListScreen(title = "지능형반도체공학과 공지사항", notices = notices, error = error)
}