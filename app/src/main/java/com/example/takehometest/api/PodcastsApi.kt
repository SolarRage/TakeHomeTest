package com.example.takehometest.api

import com.example.takehometest.api.responses.PodcastResponseItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
//интерфейс запроса ретрофит
interface PodcastsApi {
    //запрос нужного значения по базовому url
    @GET("podcasts")
    //функция с заданым ключем значением и возврашаемым листом результатов
    fun loadPodcasts(@Query("search") text: String): Single<List<PodcastResponseItem>>
}