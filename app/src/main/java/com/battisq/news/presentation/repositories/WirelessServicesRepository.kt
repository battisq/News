package com.battisq.news.presentation.repositories

import android.content.Context

interface WirelessServicesRepository {
    fun hasInternetConnection(context: Context): Boolean
    fun isGeolocationEnabled(context: Context): Boolean
}