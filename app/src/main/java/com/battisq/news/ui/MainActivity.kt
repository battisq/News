package com.battisq.news.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.battisq.news.R
import com.battisq.news.data.api.NewsApi
import com.battisq.news.data.json.NewsStory
import com.battisq.news.data.room.dao.NewsDao
import com.battisq.news.data.room.entities.NewsStoryEntity
import com.battisq.news.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
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