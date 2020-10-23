package com.battisq.news.presentation.ui.list_news.recycler.holders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.battisq.news.domain.entities.NewsStory
import com.battisq.news.databinding.ListNewsItemBinding
import com.squareup.picasso.Picasso
import java.time.format.DateTimeFormatter
import java.util.*

class ListNewsHolder(binding: ListNewsItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val image = binding.itemNewsImage
    private val title = binding.itemNewsTitle
    private val description = binding.itemNewsDescription
    private val date = binding.itemNewsDate

    @SuppressLint("NewApi")
    fun bindTo(el: NewsStory?) {
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

    companion object {
        fun create(parent: ViewGroup): ListNewsHolder {
            val binding = ListNewsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return ListNewsHolder(binding)
        }
    }
}