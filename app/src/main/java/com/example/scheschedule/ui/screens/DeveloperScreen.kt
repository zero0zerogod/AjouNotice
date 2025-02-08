package com.example.scheschedule.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheschedule.viewmodel.DeveloperViewModel

@Composable
fun DeveloperScreen(
    developerViewModel: DeveloperViewModel
) {
    val developerInfo by developerViewModel.developerInfo.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top
        ) {
            // 개발자 정보 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "개발자 소개",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    DeveloperInfoItem("이름: ${developerInfo.name}")
                    DeveloperInfoItem("소속: ${developerInfo.department}")
                    DeveloperInfoItem("학년: ${developerInfo.grade}")
                    DeveloperInfoItem("활동: ${developerInfo.role}")

                    Spacer(modifier = Modifier.height(8.dp))

                    // 이메일 정보 (복사 가능)
                    SelectionContainer {
                        Text(
                            text = "E-mail: ${developerInfo.email}",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    // 이메일 전송 버튼
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(onClick = {
                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:${developerInfo.email}")
                                putExtra(Intent.EXTRA_SUBJECT, "[피드백] 앱 관련 문의")
                            }
                            context.startActivity(emailIntent)
                        }) {
                            Icon(Icons.Default.Email, contentDescription = "Email")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "이메일 보내기")
                        }

                        OutlinedButton(onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "아주대학교 정보통신대학 공지사항 앱을 확인해보세요! 🚀")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "앱 공유하기"))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "앱 공유")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 애플리케이션 소개 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "애플리케이션 소개",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    AppDescriptionText(
                        "본 애플리케이션은 아주대학교 정보통신대학 학우들이 공지사항, 학사 일정, 학과 정보 등에 쉽게 접근할 수 있도록 개발되었습니다."
                    )

                    AppDescriptionText(
                        "현재 공지사항, 학사 일정, 학과 요람 기능을 제공하며, 지속적으로 개선해 나갈 예정입니다."
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 피드백 안내 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "의견 및 피드백",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    AppDescriptionText(
                        "앱 사용 중 개선 제안이나 버그 신고가 필요하시면, 상단의 이메일로 보내주시면 감사하겠습니다."
                    )

                    AppDescriptionText(
                        "최대한 빠르게 검토하고 조치하도록 하겠습니다."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "감사합니다.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DeveloperInfoItem(info: String) {
    Text(
        text = info, fontSize = 18.sp, modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Composable
fun AppDescriptionText(text: String) {
    Text(
        text = text, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp)
    )
}
