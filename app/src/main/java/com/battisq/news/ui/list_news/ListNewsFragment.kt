package com.battisq.news.ui.list_news

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.battisq.news.R
import com.battisq.news.data.room.entities.NewsStory
import com.battisq.news.databinding.ListNewsFragmentBinding
import com.battisq.news.domain.network.NetworkState
import com.battisq.news.domain.network.Status
import com.battisq.news.ui.activities.MainActivity
import com.battisq.news.ui.list_news.recycler.ListNewsAdapter
import com.battisq.news.ui.list_news.recycler.OnSelectedItemListener
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class ListNewsFragment : Fragment() {

    companion object {
        val TAG = this::class.simpleName
    }

    private var binging: ListNewsFragmentBinding? = null
    private val mBinding: ListNewsFragmentBinding get() = binging!!
    private lateinit var viewModel: ListNewsViewModel
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
        initViewModel()
        initFABs()
        initRecycleView()
        initRefresh()
    }

    private fun initViewModel() {
        val listener = object : FetchDataListener {
            override fun onSuccess() {

            }

            override fun onFail() {
            }
        }
        val config = Config(
            pageSize = 5,
            prefetchDistance = 5,
            enablePlaceholders = true
        )

        viewModel = getViewModel { parametersOf(config, listener) }
        lifecycle.addObserver(viewModel)
    }

    private fun initFABs() {

        fabScroll = mBinding.fabScroll
        fabScroll.setOnClickListener {
            recyclerView.scrollToPosition(0)
            fabScroll.visibility = View.GONE
        }
    }

    @SuppressLint("NewApi")
    private fun initRecycleView() {
        adapter = get { parametersOf(retryCallback) }
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

        recyclerView = mBinding.recyclerView
        recyclerView.adapter = adapter

        recyclerView.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
            if ((scrollY - oldScrollY) <= -3)
                fabScroll.visibility = View.VISIBLE
            else if ((scrollY - oldScrollY) >= 3)
                fabScroll.visibility = View.GONE
        }

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

            viewModel.refresh {
                swipeRefreshLayout.isRefreshing = false

                if (!viewModel.hasConnection())
                    showNoInternetToast()
            }
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
    }

    private fun showNoInternetToast() {
        Toast.makeText(
            context,
            getString(R.string.text_no_Internet),
            Toast.LENGTH_LONG
        ).show()
    }
}