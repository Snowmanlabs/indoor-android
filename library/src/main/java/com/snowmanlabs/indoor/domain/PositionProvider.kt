package com.snowmanlabs.indoor.domain

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log

abstract class PositionProvider(private val context: Context, private val listener: PositionListener) {

    interface PositionListener {
        fun onPositionUpdate(position: Position)
    }

    protected val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    //DEFINITIONS PROVIDER
    protected var type: String = "gps"  // or mixed
    protected var requestInterval: Long = 0
    protected var interval: Long = 2000
    protected var distance: Double = 0.toDouble()
    protected var angle: Double = 0.toDouble()

    private var lastLocation: Location? = null

    init {

        if (distance > 0 || angle > 0) {
            requestInterval = MINIMUM_INTERVAL.toLong()
        } else {
            requestInterval = interval
        }
    }

    abstract fun startUpdates()

    abstract fun stopUpdates()

    protected fun updateLocation(location: Location?) {

        if (location != null && (lastLocation == null
                || location.time - lastLocation!!.time >= interval
                || distance > 0 && DistanceCalculator.distance(location.latitude, location.longitude, lastLocation!!.latitude, location.longitude) >= distance
                || angle > 0 && Math.abs(location.bearing - lastLocation!!.bearing) >= angle)) {
            Log.i(TAG, "location new")
            lastLocation = location
            listener.onPositionUpdate(Position(location))
        } else {
            Log.i(TAG, if (location != null) "location ignored" else "location nil")
        }
    }

    companion object {

        val TAG = PositionProvider::class.java.simpleName!!

        private val MINIMUM_INTERVAL = 1000

    }

}
