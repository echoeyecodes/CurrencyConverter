package com.echoeyecodes.currencyconverter.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {
    private lateinit var retrofitBuilder: Retrofit.Builder
    private val BASE_URL = "https://freecurrencyapi.net/api/v2/"
    private lateinit var retrofit: Retrofit

    init {
        init()
    }

    companion object {
        @Volatile
        private var INSTANCE: ApiClient? = null
        fun getInstance() = INSTANCE ?: synchronized(this) {
            val instance = ApiClient()
            INSTANCE = instance
            return instance
        }
    }

    private fun init() {
        val gson = GsonBuilder().setLenient().create()
        val httpclient = OkHttpClient.Builder().readTimeout(180, TimeUnit.SECONDS)

        httpclient.addInterceptor { chain: Interceptor.Chain ->
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder().build()
            val response = chain.proceed(request)
            response
        }
        retrofitBuilder = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).client(httpclient.build())
        retrofit = retrofitBuilder.build()
    }

    fun <T> getClient(t: Class<T>?): T {
        return retrofit.create(t)
    }
}