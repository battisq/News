package com.battisq.news.ui.list_news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.battisq.news.R
import com.battisq.news.data.room.entities.NewsStoryEntity
import com.battisq.news.databinding.ListNewsFragmentBinding
import com.battisq.news.ui.MainActivity
import com.battisq.news.ui.list_news.recycler.ListNewsAdapter
import com.battisq.news.ui.list_news.recycler.NewsBoundaryCallback
import com.battisq.news.ui.list_news.recycler.OnSelectedItemListener
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf


class ListNewsFragment : Fragment() {

    companion object {
        fun newInstance() = ListNewsFragment()
    }

    private var binging: ListNewsFragmentBinding? = null
    private val mBinding: ListNewsFragmentBinding get() = binging!!
    private lateinit var viewModel: ListNewsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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
        recyclerView = mBinding.recyclerView
        val adapter = ListNewsAdapter()
        val config = Config(
            pageSize = 5,
            prefetchDistance = 5,
            enablePlaceholders = true
        )

        val hasConnection: () -> Boolean = { ListNewsViewModel.hasConnection(context!!) }
        val onFail: () -> Unit = {  }
        val boundaryCallback = get<NewsBoundaryCallback> { parametersOf(hasConnection, onFail) }

        viewModel = getViewModel { parametersOf(config, boundaryCallback) }

        viewModel.newsList.observe(this, Observer {
            adapter.submitList(it)
        })

        adapter.setOnSelectedItem(object : OnSelectedItemListener {
            override fun onSelected(position: Int, newsStory: NewsStoryEntity) {
                val bundle = Bundle()
                bundle.putString("urlSite", newsStory.url)

                (activity as MainActivity)
                    .navController
                    .navigate(R.id.action_listNewsFragment_to_itemNewsFragment, bundle)
            }
        })
        recyclerView.adapter = adapter

        swipeRefreshLayout = mBinding.swipeLayout
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binging = null
    }
}