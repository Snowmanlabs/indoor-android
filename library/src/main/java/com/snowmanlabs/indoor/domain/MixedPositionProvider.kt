package com.snowmanlabs.indoor.domain

import android.content.Context
import android.location.GpsStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

@SuppressWarnings("MissingPermission")
class MixedPositionProvider(context: Context, listener: PositionProvider.PositionListener) : PositionProvider(context, listener), LocationListener, GpsStatus.Listener {

    private var backupListener: LocationListener? = null
    private var lastFixTime: Long = 0

    override fun startUpdates() {
        lastFixTime = System.currentTimeMillis()
        locationManager.addGpsStatusListener(this)
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, requestInterval, 0f, this)
        } catch (e: IllegalArgumentException) {
            Log.w(PositionProvider.TAG, e)
        }

    }

    override fun stopUpdates() {
        locationManager.removeUpdates(this)
        locationManager.removeGpsStatusListener(this)
        stopBackupProvider()
    }

    private fun startBackupProvider() {
        Log.i(PositionProvider.TAG, "backup provider start")
        if (backupListener == null) {

            backupListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    Log.i(PositionProvider.TAG, "backup provider location")
                    updateLocation(location)
                }

                override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}

                override fun onProviderEnabled(s: String) {}

                override fun onProviderDisabled(s: String) {}
            }

            try {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, requestInterval, 0f, backupListener)
            } catch (e: IllegalArgumentException) {
                Log.w(PositionProvider.TAG, e)
            }

        }
    }

    private fun stopBackupProvider() {
        Log.i(PositionProvider.TAG, "backup provider stop")
        if (backupListener != null) {
            locationManager.removeUpdates(backupListener)
            backupListener = null
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.i(PositionProvider.TAG, "provider location")
        stopBackupProvider()
        lastFixTime = System.currentTimeMillis()
        updateLocation(location)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    override fun onProviderEnabled(provider: String) {
        Log.i(PositionProvider.TAG, "provider enabled")
        stopBackupProvider()
    }

    override fun onProviderDisabled(provider: String) {
        Log.i(PositionProvider.TAG, "provider disabled")
        startBackupProvider()
    }

    override fun onGpsStatusChanged(event: Int) {
        if (backupListener == null && System.currentTimeMillis() - lastFixTime - requestInterval > FIX_TIMEOUT) {
            startBackupProvider()
        }
    }

    companion object {

        private val FIX_TIMEOUT = 30 * 1000
    }

}
