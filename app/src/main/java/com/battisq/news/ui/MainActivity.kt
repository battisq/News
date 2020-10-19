package com.battisq.news.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.battisq.news.R
import com.battisq.news.databinding.ActivityMainBinding
import com.battisq.news.ui.map.MapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity()/*, OnMapReadyCallback*/ {

    lateinit var navController: NavController
    private var binding: ActivityMainBinding? = null
    private val mBinging: ActivityMainBinding get() = binding!!
    lateinit var mToolbar: MaterialToolbar
    private lateinit var myMapFragment: MapFragment
    private lateinit var mGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinging.root)

        mToolbar = mBinging.toolbar
        setSupportActionBar(mToolbar)

        val appBarConfiguration =
            AppBarConfiguration.Builder(R.id.navigation_news, R.id.navigation_map)
                .build()

        navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(mBinging.bottomNavigation, navController)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}