package com.devvikram.varta.data.retrofit

import com.devvikram.varta.config.constants.VartaConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private const val BASE_URL = VartaConstant.BASE_URL

        private fun getRetrofitInstance(): Retrofit {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build()


            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        private fun getBCSTEPRetrofitInstance(): Retrofit{
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
               .addInterceptor(loggingInterceptor)
               .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
               .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
               .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
               .build()

            return Retrofit.Builder()
               .baseUrl(VartaConstant.BCSTEP_BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .client(client)
               .build()
        }

        fun getBCSTEPApiService(): AppApiService {
            return getBCSTEPRetrofitInstance().create(AppApiService::class.java)
        }


        fun getApiService(): AppApiService {
            return getRetrofitInstance().create(AppApiService::class.java)
        }
    }
}