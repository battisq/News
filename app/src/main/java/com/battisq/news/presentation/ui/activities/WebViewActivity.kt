package com.battisq.news.presentation.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.battisq.news.R
import com.battisq.news.databinding.ActivityWebViewBinding
import com.google.android.material.appbar.MaterialToolbar

class WebViewActivity : AppCompatActivity() {

    private var binding: ActivityWebViewBinding? = null
    private val mBinding: ActivityWebViewBinding get() = binding!!
    private lateinit var webView: WebView
    private lateinit var urlSite: String
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        urlSite = intent.getStringExtra("urlSite")!!
        setContentView(mBinding.root)

        toolbar = mBinding.toolbar
        toolbar.title = urlSite

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initWebView(savedInstanceState)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(savedInstanceState: Bundle?) {
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_to_bottom)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}