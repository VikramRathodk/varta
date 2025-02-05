package com.devvikram.varta.data.api.services
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {
    companion object {
        private const val BASE_URL = "https://Varta-backend-production-e3c7.up.railway.app/api/"

        private fun getRetrofitInstance(): Retrofit {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY) // Choose your desired logging level, explained below

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // Add logging interceptor to the client
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

        fun getApiService(): AppApiService {
            return getRetrofitInstance().create(AppApiService::class.java)
        }
    }
}