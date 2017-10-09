package com.snowmanlabs.indoor.domain

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import android.preference.PreferenceManager
import android.util.Log

import com.snowmanlabs.indoor.IndoorPresenter


abstract class PositionProvider(private val context: Context, private val listener: PositionListener) {

    interface PositionListener {
        fun onPositionUpdate(position: Position)
    }

    protected val locationManager: LocationManager

    private val deviceId: String
    protected var type: String
    protected var requestInterval: Long = 0
    protected var interval: Long = 0
    protected var distance: Double = 0.toDouble()
    protected var angle: Double = 0.toDouble()

    private var lastLocation: Location? = null

    init {

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        deviceId = preferences.getString(IndoorPresenter.KEY_DEVICE, "undefined")
        interval = java.lang.Long.parseLong(preferences.getString(IndoorPresenter.KEY_INTERVAL, "2")) * 1000
        distance = Integer.parseInt(preferences.getString(IndoorPresenter.KEY_DISTANCE, "0")).toDouble()
        angle = Integer.parseInt(preferences.getString(IndoorPresenter.KEY_ANGLE, "0")).toDouble()

        if (distance > 0 || angle > 0) {
            requestInterval = MINIMUM_INTERVAL.toLong()
        } else {
            requestInterval = interval
        }

        type = preferences.getString(IndoorPresenter.KEY_PROVIDER, "mixed")
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
            listener.onPositionUpdate(Position(deviceId, location, getBatteryLevel(context)))
        } else {
            Log.i(TAG, if (location != null) "location ignored" else "location nil")
        }
    }

    companion object {

        public val TAG = PositionProvider::class.java.simpleName

        private val MINIMUM_INTERVAL = 1000

        fun getBatteryLevel(context: Context): Double {
            val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            if (batteryIntent != null) {
                val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 1)
                return level * 100.0 / scale
            }
            return 0.0
        }
    }

}
