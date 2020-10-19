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
import com.battisq.news.data.room.entities.NewsStory
import com.battisq.news.databinding.ListNewsItemBinding
import com.squareup.picasso.Picasso
import java.time.format.DateTimeFormatter
import java.util.*

class ListNewsAdapter() :
    PagedListAdapter<NewsStory, ListNewsHolder>(DIFF_CALLBACK) {

    private var onSelectedItemListener: OnSelectedItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNewsHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)

        val binding: ListNewsItemBinding =
            ListNewsItemBinding.inflate(inflater, parent, false)

        val holder = ListNewsHolder(binding)

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition

            if (position != RecyclerView.NO_POSITION)
                runOnSelectedItem(position, getItem(position)!!)
        }

        return holder
    }

    private fun runOnSelectedItem(position: Int, item: NewsStory) =
        onSelectedItemListener?.onSelected(position, item)

    fun setOnSelectedItem(listener: OnSelectedItemListener) {
        onSelectedItemListener = listener
    }

    override fun onBindViewHolder(holder: ListNewsHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<NewsStory>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldNewsStory: NewsStory,
                newNewsStory: NewsStory
            ) = oldNewsStory.url == newNewsStory.url

            override fun areContentsTheSame(
                oldNewsStory: NewsStory,
                newNewsStory: NewsStory
            ) = oldNewsStory == newNewsStory
        }
    }
}

class ListNewsHolder(private val binding: ListNewsItemBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("NewApi")
    fun bind(el: NewsStory?) {
        if (el?.imageToUrl != null && !el.imageToUrl.isBlank())
            Picasso.get().load(el.imageToUrl).into(binding.itemNewsImage)

        binding.itemNewsTitle.text = el?.title
        binding.itemNewsDescription.text = el?.description
        binding.itemNewsDate.text = el?.date?.format(
            DateTimeFormatter
                .ofPattern(
                    "HH:mm dd.MM.yyyy",
                    Locale.ENGLISH
                )
        )
    }
}

interface OnSelectedItemListener {
    fun onSelected(position: Int, newsStory: NewsStory)
}