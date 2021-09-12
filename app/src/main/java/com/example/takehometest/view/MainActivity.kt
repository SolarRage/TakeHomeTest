package com.example.takehometest.view

import android.os.Bundle
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.takehometest.R
import com.example.takehometest.view.PodcastsAdapter.PodcastItemModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainView {

    private val presenter: MainPresenter = MainPresenterImpl()

    private val podcastsAdapter = PodcastsAdapter(::handleOnClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        presenter.bind(this)
    }

    private fun initViews() {
        rvPodcasts.addItemDecoration(DividerItemDecoration(this, VERTICAL))
        rvPodcasts.adapter = podcastsAdapter
        etPodcasts.doOnTextChanged { text, _, _, _ -> presenter.onTextChange(text.toString()) }
    }


    private fun handleOnClick(podcastItemModel: PodcastItemModel) {

    }

    override fun setPodcasts(podcasts: List<PodcastItemModel>) {
        podcastsAdapter.setPodcasts(podcasts)
    }


    override fun showProgress(isShown: Boolean) {
        progressBar.isVisible = isShown
    }


    override fun showEmptyView(isShown: Boolean) {
        noResult.isVisible = isShown
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}