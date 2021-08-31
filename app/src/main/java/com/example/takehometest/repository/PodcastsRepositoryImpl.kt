package com.example.takehometest.repository

import com.example.takehometest.BuildConfig
import com.example.takehometest.api.PodcastsApi
import com.example.takehometest.api.RxCallAdapterFactory
import com.example.takehometest.api.responses.PodcastResponseItem
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PodcastsRepositoryImpl : PodcastsRepository {

    private val podcastsApi: PodcastsApi by lazy { createApi() }

    override fun getPodcasts(text: String): Single<List<PodcastResponseItem>> {
        return podcastsApi.loadPodcasts(text)
    }

    private fun createApi() = createRetrofit().create(PodcastsApi::class.java)

    private fun createRetrofit() = Retrofit.Builder()
            //передаем ссылку
        .baseUrl(BASE_URL)
            //
        .addCallAdapterFactory(RxCallAdapterFactory())
            //создаем конвертор GSON
        .addConverterFactory(GsonConverterFactory.create(createGson()))
            //Кастом клиент okhttp
        .client(createOkHttpClient())
        .build()

    /**
     * Кастом клиент. Устанавливает таймауты по соединению и чтению данных на 1 мин.
     * Перехватывает коды соединения и выводит полным BODY
     */
    private fun createOkHttpClient() = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }).build()
    //создаем GSON с XML формата
    private fun createGson(): Gson = GsonBuilder().create()


    private companion object {
        //
        private const val BASE_URL = "https://601f1754b5a0e9001706a292.mockapi.io/"
    }
}