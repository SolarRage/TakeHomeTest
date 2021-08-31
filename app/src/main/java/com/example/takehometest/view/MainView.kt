package com.example.takehometest.view

import com.example.takehometest.view.PodcastsAdapter.PodcastItemModel

interface MainView {
    fun setPodcasts(podcasts: List<PodcastItemModel>)

    fun showProgress(isShown: Boolean)

    fun showEmptyView(isShown: Boolean)
}