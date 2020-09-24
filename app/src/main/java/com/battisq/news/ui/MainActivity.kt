package com.battisq.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.battisq.news.R
import com.battisq.news.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private var binding: ActivityMainBinding? = null
    private val mBinging: ActivityMainBinding get() = binding!!
    private lateinit var mToolbar: Toolbar

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
        title = getString(R.string.title)
    }




    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}