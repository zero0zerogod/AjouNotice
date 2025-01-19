package com.example.scheschedule.network

import com.example.scheschedule.BuildConfig

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    // Retrofit 인스턴스 생성
    val api: NoticeApi by lazy {
        createRetrofitInstance()
    }

    private fun createRetrofitInstance(): NoticeApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoticeApi::class.java)
    }

}
