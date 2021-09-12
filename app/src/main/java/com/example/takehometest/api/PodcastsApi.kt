package com.example.takehometest.api

import com.example.takehometest.api.responses.PodcastResponseItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PodcastsApi {
    @GET("podcasts")
    fun loadPodcasts(@Query("search") text: String): Single<List<PodcastResponseItem>>
}