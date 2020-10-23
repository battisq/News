package com.battisq.news.presentation.ui.list_news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.battisq.news.R
import com.battisq.news.domain.entities.NewsStory
import com.battisq.news.databinding.ListNewsFragmentBinding
import com.battisq.news.domain.network.NetworkState
import com.battisq.news.domain.network.Status
import com.battisq.news.presentation.ui.activities.MainActivity
import com.battisq.news.presentation.ui.list_news.recycler.ListNewsAdapter
import com.battisq.news.presentation.ui.list_news.recycler.OnSelectedItemListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.getViewModel

class ListNewsFragment : Fragment() {

    companion object {
        val TAG = this::class.simpleName
    }

    private var binging: ListNewsFragmentBinding? = null
    private val mBinding: ListNewsFragmentBinding get() = binging!!
    private var viewModel: ListNewsViewModel = get()
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var fabScroll: FloatingActionButton
    private lateinit var adapter: ListNewsAdapter
    private lateinit var listNewsObserver: Observer<PagedList<NewsStory>>
    private lateinit var networkStateObserver: Observer<NetworkState>

    private val retryCallback: () -> Unit = {
        if (viewModel.hasConnection()) {
            viewModel.retry {}
        } else
            showNoInternetToast()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binging = ListNewsFragmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        initAdapter()
        initViewModel()
        initRecycleView()
        initFABs()
        initRefresh()
    }

    private fun initViewModel() {
        val listener = object : OnFetchDataListener {
            override fun onSuccess() {}
            override fun onFail(message: String?) {}
            override fun onEndData() {
                showEndNewsToast()
            }
        }
        viewModel.setOnFetchDataListener(listener)
        lifecycle.addObserver(viewModel)

        networkStateObserver = Observer {
            if (it.status == Status.FAILED)
                showNoInternetToast()

            adapter.setNetworkState(it)
        }
        viewModel.networkState.observe(this, networkStateObserver)

        listNewsObserver = Observer {
            adapter.submitList(it)
        }
        viewModel.newsList.observe(this, listNewsObserver)
    }

    @SuppressLint("NewApi")
    private fun initFABs() {
        fabScroll = mBinding.fabScroll
        fabScroll.setOnClickListener {
            recyclerView.scrollToPosition(0)
            fabScroll.visibility = View.GONE
        }

        recyclerView.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
            if ((scrollY - oldScrollY) <= -3)
                fabScroll.visibility = View.VISIBLE
            else if ((scrollY - oldScrollY) >= 3)
                fabScroll.visibility = View.GONE
        }
    }

    private fun initAdapter() {
        adapter = ListNewsAdapter(retryCallback)
        adapter.setOnSelectedItem(object : OnSelectedItemListener {
            override fun onSelected(position: Int, newsStory: NewsStory) {
                if (!viewModel.hasConnection()) {
                    showNoInternetToast()
                    return
                }

                val bundle = Bundle()
                bundle.putString("urlSite", newsStory.url)

                (activity as MainActivity)
                    .navController
                    .navigate(R.id.action_navigation_news_to_webViewActivity, bundle)
            }
        })
    }

    private fun initRecycleView() {
        recyclerView = mBinding.recyclerView
        recyclerView.adapter = adapter

        if (viewModel.recyclerViewState != null)
            recyclerView.layoutManager?.onRestoreInstanceState(viewModel.recyclerViewState)
    }

    private fun initRefresh() {
        swipeRefreshLayout = mBinding.swipeLayout
        swipeRefreshLayout.setOnRefreshListener {
            if (!viewModel.hasConnection()) {
                swipeRefreshLayout.isRefreshing = false
                showNoInternetToast()
                return@setOnRefreshListener
            }
            viewModel.refresh().also { swipeRefreshLayout.isRefreshing = false }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binging = null
        recyclerView.adapter = null
        viewModel.newsList.removeObserver(listNewsObserver)
        viewModel.networkState.removeObserver(networkStateObserver)
    }

    private fun showNoInternetToast() {
        Toast.makeText(
            context,
            getString(R.string.text_no_Internet),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showEndNewsToast() {
        Toast.makeText(
            context,
            getString(R.string.text_end_news),
            Toast.LENGTH_LONG
        ).show()
    }
}