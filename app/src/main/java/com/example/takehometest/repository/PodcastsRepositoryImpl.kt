package com.example.takehometest.repository

import com.example.takehometest.BuildConfig
import com.example.takehometest.api.PodcastsApi
import com.example.takehometest.api.responses.PodcastResponseItem
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PodcastsRepositoryImpl : PodcastsRepository {

    private val podcastsApi: PodcastsApi by lazy { createApi() }

    override fun getPodcasts(text: String): Single<List<PodcastResponseItem>> {
        return podcastsApi.loadPodcasts(text)
    }

    private fun createApi() = createRetrofit().create(PodcastsApi::class.java)

    private fun createRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(createGson()))
        .client(createOkHttpClient())
        .build()


    private fun createOkHttpClient() = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }).build()

    private fun createGson(): Gson = GsonBuilder().create()


    private companion object {
        //
        private const val BASE_URL = "https://601f1754b5a0e9001706a292.mockapi.io/"
    }
}