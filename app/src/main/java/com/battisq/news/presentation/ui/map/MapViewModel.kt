package com.battisq.news.presentation.ui.map

import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import com.battisq.news.app.application.App
import com.battisq.news.presentation.repositories.WirelessServicesRepository

class MapViewModel(
    application: Application,
    private val wirelessServicesRepository: WirelessServicesRepository
) : AndroidViewModel(application) {

    fun isGeolocationEnabled(): Boolean {
        return wirelessServicesRepository.isGeolocationEnabled(getApplication())
    }
}