package com.example.scheschedule.ui.notice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scheschedule.ui.viewmodel.NoticeViewModel

@Composable
fun GeneralNoticeScreen(viewModel: NoticeViewModel = viewModel()) {
    val notices by viewModel.generalNotices.collectAsState()
    val error by viewModel.generalError.collectAsState()

    NoticeListScreen(
        title = "일반 공지사항", notices = notices, error = error
    )
}
