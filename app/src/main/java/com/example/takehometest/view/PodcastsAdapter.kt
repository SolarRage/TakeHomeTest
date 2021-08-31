package com.example.takehometest.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takehometest.R
import com.example.takehometest.view.PodcastsAdapter.PodcastViewHolder
import kotlinx.android.synthetic.main.item_podcast.view.*

class PodcastsAdapter(
    private val onItemClick: (PodcastItemModel) -> Unit
) : RecyclerView.Adapter<PodcastViewHolder>() {

    private var podcasts: List<PodcastItemModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PodcastViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_podcast, parent, false),
        onItemClick
    )

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        holder.bind(podcasts[position])
    }

    override fun getItemCount(): Int = podcasts.size

    fun setPodcasts(podcasts: List<PodcastItemModel>) {
        val result = DiffUtil.calculateDiff(PodcastsDiffUtilCallback(this.podcasts, podcasts))
        result.dispatchUpdatesTo(this)
        this.podcasts = podcasts

    }


    data class PodcastItemModel(
        val id: String,
        val name: String?,
        val author: String?,
        val category: String?,
        val imageURL: String?
    )


    class PodcastViewHolder(
        view: View,
        private val onItemClick: (PodcastItemModel) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        fun bind(item: PodcastItemModel) {
            itemView.apply {
                podcastPublisher.text = item.author
                podcastCategory.text = item.category
                podcastCategory.isVisible = !item.category.isNullOrBlank()
                podcastName.text = item.name
                setOnClickListener { onItemClick(item) }
                Glide.with(context).load(item.imageURL).into(podcastImage)
            }

        }
    }


    private class PodcastsDiffUtilCallback(
        private var oldItems: List<PodcastItemModel>,
        private var newItems: List<PodcastItemModel>
    ) : DiffUtil.Callback() {


        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size


        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }
}