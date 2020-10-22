package com.battisq.news.ui.list_news.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.battisq.news.R
import com.battisq.news.data.room.entities.NewsStory
import com.battisq.news.databinding.ListNewsItemBinding
import com.battisq.news.domain.network.NetworkState
import com.battisq.news.ui.list_news.recycler.holders.ListNewsHolder
import com.battisq.news.ui.list_news.recycler.holders.NetworkStateItemViewHolder

class ListNewsAdapter(private val retryCallback: () -> Unit) :
    PagedListAdapter<NewsStory, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var onSelectedItemListener: OnSelectedItemListener? = null
    private var networkState: NetworkState? = null
    private var count: Int = 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.list_news_item -> (holder as ListNewsHolder).bindTo(getItem(position))
            R.layout.network_state_item -> {
//                if (count == 1)
                    (holder as NetworkStateItemViewHolder).bindTo(
                        networkState
                    )
//                else return
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val holder: RecyclerView.ViewHolder

        when (viewType) {
            R.layout.list_news_item -> {
                holder = ListNewsHolder.create(parent)
                holder.itemView.setOnClickListener {
                    val position = holder.adapterPosition

                    if (position != RecyclerView.NO_POSITION)
                        runOnSelectedItem(position, getItem(position)!!)
                }
            }
            R.layout.network_state_item -> holder =
                NetworkStateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }

        return holder
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            count++
            Log.e(this::class.simpleName, "network_state")
            R.layout.network_state_item
        } else
            R.layout.list_news_item
    }

    private fun runOnSelectedItem(position: Int, item: NewsStory) =
        onSelectedItemListener?.onSelected(position, item)

    fun setOnSelectedItem(listener: OnSelectedItemListener) {
        onSelectedItemListener = listener
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = networkState
        val hadExtraRow = hasExtraRow()
        networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            // Переход из
            if (hadExtraRow)
                notifyItemRemoved(super.getItemCount())
            else
                notifyItemInserted(super.getItemCount())
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
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

interface OnSelectedItemListener {
    fun onSelected(position: Int, newsStory: NewsStory)
}