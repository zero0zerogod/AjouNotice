package com.example.scheschedule.ui.notice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scheschedule.ui.viewmodel.NoticeViewModel

@Composable
fun ScholarshipNoticeScreen(viewModel: NoticeViewModel = viewModel()) {
    val notices by viewModel.scholarshipNotices.collectAsState()
    val error by viewModel.scholarshipError.collectAsState()
    NoticeListScreen(title = "장학 공지사항", notices = notices, error = error)
}