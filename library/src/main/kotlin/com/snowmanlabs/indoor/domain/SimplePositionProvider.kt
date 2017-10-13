package com.snowmanlabs.indoor.domain

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
@SuppressWarnings("MissingPermission")
class SimplePositionProvider(context: Context, listener: PositionProvider.PositionListener) : PositionProvider(context, listener), LocationListener {

    init {
        if (type != LocationManager.NETWORK_PROVIDER) {
            type = LocationManager.GPS_PROVIDER
        }
    }

    override fun startUpdates() {
        try {
            locationManager.requestLocationUpdates(type, requestInterval, 0f, this)
        } catch (e: IllegalArgumentException) {
            Log.w(PositionProvider.Companion.TAG, e)
        }

    }

    override fun stopUpdates() {
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        updateLocation(location)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

}
