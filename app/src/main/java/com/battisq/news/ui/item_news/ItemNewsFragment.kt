package com.battisq.news.ui.item_news

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.battisq.news.R
import com.battisq.news.databinding.ItemNewsFragmentBinding
import com.battisq.news.ui.MainActivity

class ItemNewsFragment : Fragment() {

    private var binding: ItemNewsFragmentBinding? = null
    private val mBinding: ItemNewsFragmentBinding get() = binding!!
    private lateinit var webView: WebView
    private lateinit var urlSite: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemNewsFragmentBinding.inflate(inflater, container, false)

        //initActionMode()
        initWebView(savedInstanceState)
        (activity as MainActivity).mToolbar.title = urlSite
        return mBinding.root
    }

    private fun initActionMode() {
        val callback = object : androidx.appcompat.view.ActionMode.Callback {

            var statusBarColor: Int = 0

            override fun onCreateActionMode(
                mode: androidx.appcompat.view.ActionMode?,
                menu: Menu?
            ): Boolean {
                statusBarColor = activity?.window?.statusBarColor!!
                activity?.window?.statusBarColor = Color.BLACK

                return true
            }

            override fun onPrepareActionMode(
                mode: androidx.appcompat.view.ActionMode?,
                menu: Menu?
            ): Boolean {
                return false
            }

            override fun onActionItemClicked(
                mode: androidx.appcompat.view.ActionMode?,
                item: MenuItem?
            ): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: androidx.appcompat.view.ActionMode?) {
                activity?.window?.statusBarColor = statusBarColor

                (activity as MainActivity)
                    .navController
                    .navigate(R.id.action_navigation_news_story_to_navigation_news)
            }
        }

        (activity as MainActivity).startSupportActionMode(callback)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(savedInstanceState: Bundle?) {
        urlSite = arguments?.getString("urlSite")!!

        webView = mBinding.webView
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }
        }

        if (savedInstanceState == null)
            webView.loadUrl(urlSite)
        else
            webView.restoreState(savedInstanceState.getBundle("webViewState")!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = Bundle()
        webView.saveState(bundle)
        outState.putBundle("webViewState", bundle)
    }
}