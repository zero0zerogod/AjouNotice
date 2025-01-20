package com.example.scheschedule.network

import com.example.scheschedule.model.Notice
import retrofit2.http.GET

interface NoticeApi {
    @GET("api/notices/general")
    suspend fun getGeneralNotices(): List<Notice>

    @GET("api/notices/scholarship")
    suspend fun getScholarshipNotices(): List<Notice>

    @GET("api/notices/dormitory")
    suspend fun getDormitoryNotices(): List<Notice>

    @GET("api/notices/department_ece")
    suspend fun getECENotices(): List<Notice>

    @GET("api/notices/department_aisemi")
    suspend fun getAiSemiNotices(): List<Notice>
}
