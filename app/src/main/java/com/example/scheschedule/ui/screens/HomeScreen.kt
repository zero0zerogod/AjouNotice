package com.example.scheschedule.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.JavascriptInterface
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val isLoading by scheduleViewModel.isLoading.collectAsState()
    val isError by scheduleViewModel.isError.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> { // ✅ 로딩 중 UI
                LoadingScreen()
            }
            isError -> { // ✅ 에러 발생 시 UI
                ErrorScreen { scheduleViewModel.loadSchedule(2025) }
            }
            else -> { // ✅ 정상적으로 로드된 경우 웹뷰 표시
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
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
    }
}

/** ✅ 로딩 중 화면 */
@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

/** ✅ 에러 화면 (재시도 버튼 포함) */
@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "일정을 불러오는 중 오류가 발생했습니다.", color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(text = "다시 시도", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}
