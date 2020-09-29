package com.battisq.news.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.battisq.news.R
import com.battisq.news.databinding.ActivityMainBinding
import com.battisq.news.ui.list_news.recycler.NewsBoundaryCallback
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private var binding: ActivityMainBinding? = null
    private val mBinging: ActivityMainBinding get() = binding!!
    private lateinit var mToolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinging.root)

        mToolbar = mBinging.toolbar
        navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
        setSupportActionBar(mToolbar)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}