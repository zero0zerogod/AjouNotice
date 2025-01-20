package com.example.scheschedule.repository

import com.example.scheschedule.model.Notice
import com.example.scheschedule.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoticeRepository {

    // 공지사항들 가져오는 메서드
    suspend fun getGeneralNotices(): List<Notice> {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getGeneralNotices()
        }
    }

    suspend fun getScholarshipNotices(): List<Notice> {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getScholarshipNotices()
        }
    }

    suspend fun getDormitoryNotices(): List<Notice> {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getDormitoryNotices()
        }
    }

    suspend fun getECENotices(): List<Notice> {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getECENotices()
        }
    }

    suspend fun getAiSemiNotices(): List<Notice> {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getAiSemiNotices()
        }
    }



    // 필요하다면 더 많은 Repository 로직(캐싱, DB 저장 등)을 여기에 작성
}
