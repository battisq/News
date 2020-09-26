package com.battisq.news.ui.item_news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.battisq.news.R
import com.battisq.news.databinding.ItemNewsFragmentBinding

class ItemNewsFragment : Fragment() {

    private var binding: ItemNewsFragmentBinding? = null
    private val mBinding: ItemNewsFragmentBinding get() = binding!!
    private lateinit var webView: WebView
    private lateinit var urlSite: String

    @SuppressLint("SetJavaScriptEnabled", "RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemNewsFragmentBinding.inflate(inflater, container, false)
        urlSite = arguments?.getString("urlSite")!!

        webView = mBinding.webView
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }
        }

        webView.loadUrl(urlSite)

        setHasOptionsMenu(true)

        return mBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    
}