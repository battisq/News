package com.battisq.news.ui.item_news

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.battisq.news.R
import com.battisq.news.databinding.ItemNewsFragmentBinding

class ItemNewsFragment : Fragment() {

    companion object {
        fun newInstance() = ItemNewsFragment()
    }

    private var binding: ItemNewsFragmentBinding? = null
    private val mBinding: ItemNewsFragmentBinding get() = binding!!
    private lateinit var viewModel: ItemNewsViewModel
    private lateinit var webView: WebView
    private lateinit var urlSite: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemNewsFragmentBinding.inflate(inflater, container, false)
        urlSite = arguments?.getString("urlSite")!!
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initialization() {
        webView = mBinding.webView

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }
        }

        webView.webChromeClient = WebChromeClient()
        webView.loadUrl(urlSite)
    }
}