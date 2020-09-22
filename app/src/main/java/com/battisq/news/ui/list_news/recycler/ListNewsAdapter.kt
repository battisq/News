package com.battisq.news.ui.list_news.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.battisq.news.R
import com.battisq.news.data.room.entities.NewsStoryEntity
import java.time.format.DateTimeFormatter
import java.util.*

class ListNewsAdapter() :
    PagedListAdapter<NewsStoryEntity, ListNewsHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNewsHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_news_large_item, parent, false)
        return ListNewsHolder(view)
    }

    override fun onBindViewHolder(holder: ListNewsHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<NewsStoryEntity>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldNewsStory: NewsStoryEntity,
                newNewsStory: NewsStoryEntity
            ) = oldNewsStory.id == newNewsStory.id

            override fun areContentsTheSame(
                oldNewsStory: NewsStoryEntity,
                newNewsStory: NewsStoryEntity
            ) = oldNewsStory == newNewsStory
        }
    }
}

class ListNewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var image: ImageView = itemView.findViewById(R.id.item_news_image)
    var title: TextView = itemView.findViewById(R.id.item_news_title)
    var description: TextView = itemView.findViewById(R.id.item_news_description)
    var date: TextView = itemView.findViewById(R.id.item_news_date)

    @SuppressLint("NewApi")
    fun bind(el: NewsStoryEntity?) {
        //TODO - image
        title.text = el?.title
        description.text = el?.description
        date.text = el?.date?.format(
            DateTimeFormatter
                .ofPattern(
                    "HH:mm dd.MM.yyyy",
                    Locale.ENGLISH
                )
        )
    }
}