package com.example.takehometest.repository

import com.example.takehometest.api.responses.PodcastResponseItem
import io.reactivex.Single

interface PodcastsRepository {
    fun getPodcasts(text: String): Single<List<PodcastResponseItem>>
}