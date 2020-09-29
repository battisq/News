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
import com.squareup.picasso.Picasso
import java.time.format.DateTimeFormatter
import java.util.*

class ListNewsAdapter() :
    PagedListAdapter<NewsStoryEntity, ListNewsHolder>(DIFF_CALLBACK) {

    private var onSelectedItemListener: OnSelectedItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNewsHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_news_item, parent, false)

        val holder = ListNewsHolder(view)

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition

            if (position != RecyclerView.NO_POSITION)
                runOnSelectedItem(position, getItem(position)!!)
        }

        return holder
    }

    private fun runOnSelectedItem(position: Int, item: NewsStoryEntity) =
        onSelectedItemListener?.onSelected(position, item)

    fun setOnSelectedItem(listener: OnSelectedItemListener) {
        onSelectedItemListener = listener
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
        if (el?.imageToUrl != null && !el.imageToUrl.isBlank())
            Picasso.get().load(el.imageToUrl).into(image)

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

interface OnSelectedItemListener {
    fun onSelected(position: Int, newsStory: NewsStoryEntity)
}