package com.battisq.news.ui.list_news

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.battisq.news.R
import com.battisq.news.data.room.entities.NewsStory
import com.battisq.news.databinding.ListNewsFragmentBinding
import com.battisq.news.ui.MainActivity
import com.battisq.news.ui.list_news.recycler.ListNewsAdapter
import com.battisq.news.ui.list_news.recycler.OnSelectedItemListener
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class ListNewsFragment : Fragment() {

    private var binging: ListNewsFragmentBinding? = null
    private val mBinding: ListNewsFragmentBinding get() = binging!!
    private lateinit var viewModel: ListNewsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var fabRetry: ExtendedFloatingActionButton
    private lateinit var fabScroll: FloatingActionButton
    private val adapter: ListNewsAdapter by inject()
    private lateinit var observer: Observer<PagedList<NewsStory>>

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
                fabRetry.visibility = View.VISIBLE
            }
        }
        val config = Config(
            pageSize = 5,
            prefetchDistance = 5,
            enablePlaceholders = true
        )

        observer = Observer {
            adapter.submitList(it)
        }
        viewModel = getViewModel { parametersOf(config, listener) }
        viewModel.newsList.observe(this, observer)
        lifecycle.addObserver(viewModel)
    }

    private fun initFABs() {
        fabRetry = mBinding.fabRetry
        fabRetry.setOnClickListener {
            if (viewModel.hasConnection()) {
                viewModel.retry {
                    fabRetry.visibility = View.GONE
                }
            } else
                showNoInternetToast()
        }

        fabScroll = mBinding.fabScroll
        fabScroll.setOnClickListener {
            recyclerView.scrollToPosition(0)
            fabScroll.visibility = View.GONE
        }
    }

    @SuppressLint("NewApi")
    private fun initRecycleView() {
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
                    .navigate(R.id.action_listNewsFragment_to_itemNewsFragment, bundle)
            }
        })

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
        viewModel.newsList.removeObserver(observer)
    }

    private fun showNoInternetToast() {
        Toast.makeText(
            context,
            getString(R.string.text_no_Internet),
            Toast.LENGTH_LONG
        ).show()
    }
}