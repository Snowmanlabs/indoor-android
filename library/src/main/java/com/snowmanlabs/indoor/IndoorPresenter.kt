package com.snowmanlabs.indoor

import android.content.Context
import android.os.Handler
import android.util.Log
import com.snowmanlabs.indoor.domain.*

class IndoorPresenter(private val context: Context, private val mView: IIndoorView?) : PositionProvider.PositionListener {

    private val handler: Handler = Handler()
    private val positionProvider: PositionProvider
    var isRunning = false

    init {

        //MIXED
//        positionProvider =  MixedPositionProvider(context, this)

        // GPS
        positionProvider = SimplePositionProvider(context, this)

    }

    fun start() {
        try {
            positionProvider.startUpdates()
            isRunning = true
        } catch (e: SecurityException) {
            Log.w(TAG, e)
        }
    }

    fun stop() {
        try {
            isRunning = false
            positionProvider.stopUpdates()
        } catch (e: SecurityException) {
            Log.w(TAG, e)
        }

        handler.removeCallbacksAndMessages(null)
    }

    override fun onPositionUpdate(position: Position) {

        if (position != null) {
            mView?.setMyPosition(position)

            log("write", position)
        }
    }

    private fun log(action: String, position: Position?) {
        var action = action
        if (position != null) {
            action += " (" +
                    " time:" + position.time!!.time / 1000 +
                    " lat:" + position.latitude +
                    " lon:" + position.longitude + ")"
        }
        Log.d(TAG, action)
    }

    companion object {
        private val TAG = IndoorPresenter::class.java.simpleName
    }
}
