package com.example.takehometest.api.responses

import com.google.gson.annotations.SerializedName

data class PodcastResponseItem(
    //поле в котором происходит поиск
    @SerializedName("title")
    val title: String?,
    @SerializedName("images")
    val images: PodcastResponseImages?,
    @SerializedName("publisherName")
    val publisherName: String?,
    @SerializedName("categoryName")
    val categoryName: String?,
    @SerializedName("id")
    val id: String
)

data class PodcastResponseImages(
    @SerializedName("thumbnail")
    val thumbnail: String?
)