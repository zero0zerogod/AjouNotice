package com.example.scheschedule.widget

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient

class WrapContentWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    init {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                post {
                    evaluateJavascript(
                        "(function() { return document.body.scrollHeight; })();"
                    ) { height ->
                        val newHeight = height.toIntOrNull() ?: return@evaluateJavascript
                        layoutParams = layoutParams.apply {
                            this.height = newHeight
                        }
                        requestLayout() // 레이아웃을 다시 측정하여 빈 공간 해결
                    }
                }
            }
        }
    }
}
