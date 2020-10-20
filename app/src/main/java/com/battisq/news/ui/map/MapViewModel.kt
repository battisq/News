package com.battisq.news.ui.map

import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import com.battisq.news.ui.App

class MapViewModel(application: Application) : AndroidViewModel(application) {

    fun isGeolocationEnabled(): Boolean {
        val enabled =
            (getApplication<App>().getSystemService(Context.LOCATION_SERVICE) as LocationManager)
            .isProviderEnabled(LocationManager.GPS_PROVIDER)

        return enabled
    }
}