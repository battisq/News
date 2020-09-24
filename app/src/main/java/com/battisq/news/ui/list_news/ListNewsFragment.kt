package com.battisq.news.ui.list_news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.battisq.news.R
import com.battisq.news.data.room.entities.NewsStoryEntity
import com.battisq.news.databinding.ListNewsFragmentBinding
import com.battisq.news.ui.MainActivity
import com.battisq.news.ui.list_news.recycler.ListNewsAdapter
import com.battisq.news.ui.list_news.recycler.MainThreadExecutor
import com.battisq.news.ui.list_news.recycler.NewsPositionalDataSource
import com.battisq.news.ui.list_news.recycler.OnSelectedItemListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ListNewsFragment : Fragment() {

    companion object {
        fun newInstance() = ListNewsFragment()
    }

    private var binging: ListNewsFragmentBinding? = null
    private val mBinding: ListNewsFragmentBinding get() = binging!!
    private lateinit var viewModel: ListNewsViewModel
    private lateinit var recyclerView: RecyclerView

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
            prefetchDistance = 1,
            enablePlaceholders = false
        )
        viewModel = getViewModel { parametersOf(config) }
        viewModel.newsList.observe(this, Observer { adapter.submitList(it) })
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

    }
}