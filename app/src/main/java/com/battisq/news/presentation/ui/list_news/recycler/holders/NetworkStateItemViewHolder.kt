package com.battisq.news.presentation.ui.list_news.recycler.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.battisq.news.R
import com.battisq.news.databinding.NetworkStateItemBinding
import com.battisq.news.domain.network.NetworkState
import com.battisq.news.domain.network.Status

class NetworkStateItemViewHolder(binding: NetworkStateItemBinding, private val retryCallback: () -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    private val progressBar = binding.progressBar
    private val retry = binding.retryButton
    private val errorMessage = binding.errorMessage

    init {
        retry.setOnClickListener { retryCallback() }
    }

    fun bindTo(networkState: NetworkState?) {
        progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
        retry.visibility = toVisibility(networkState?.status == Status.FAILED)
        errorMessage.visibility = toVisibility(networkState?.message != null)
//        errorMessage.text = networkState?.message
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateItemViewHolder {
            val binding = NetworkStateItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return NetworkStateItemViewHolder(binding, retryCallback)
        }

        fun toVisibility(constraint : Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}