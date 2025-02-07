package com.example.scheschedule.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.JavascriptInterface
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scheschedule.viewmodel.AjouScheduleViewModel
import com.example.scheschedule.widget.WrapContentWebView

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HomeScreen(
    scheduleViewModel: AjouScheduleViewModel = viewModel()
) {
    val scheduleHtml by scheduleViewModel.scheduleHtml.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // ✅ 웹뷰 추가 (항상 표시)
            AndroidView(factory = { context ->
                WrapContentWebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = object : WebViewClient() {}

                    addJavascriptInterface(object {
                        @JavascriptInterface
                        fun changeYear(newYear: Int) {
                            scheduleViewModel.loadSchedule(newYear)
                        }
                    }, "Android")

                    loadDataWithBaseURL(
                        "https://www.ajou.ac.kr/", scheduleHtml, "text/html", "UTF-8", null
                    )
                }
            }, update = { webView ->
                webView.loadDataWithBaseURL(
                    "https://www.ajou.ac.kr/", scheduleHtml, "text/html", "UTF-8", null
                )
            })
        }
    }
}
