package com.snowmanlabs.indoor

import android.content.Context
import android.os.Handler
import android.util.Log
import com.snowmanlabs.indoor.domain.*
import java.io.Serializable

class IndoorPresenter(private val context: Context, private val mView: IIndoorView?) : PositionProvider.PositionListener , Serializable{

    private val handler: Handler = Handler()
    private val positionProvider: PositionProvider

    init {

        positionProvider =  MixedPositionProvider(context, this)

        //positionProvider = SimplePositionProvider(context, this)

    }

    fun start() {
        try {
            positionProvider.startUpdates()
        } catch (e: SecurityException) {
            Log.w(TAG, e)
        }
    }

    fun stop() {
        try {
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
                    "id:" + position.id +
                    " time:" + position.time!!.time / 1000 +
                    " lat:" + position.latitude +
                    " lon:" + position.longitude + ")"
        }
        Log.d(TAG, action)
    }

    companion object {

        private val TAG = IndoorPresenter::class.java.simpleName

        var KEY_DEVICE = "id"
        var KEY_INTERVAL = "interstatic String"
        var KEY_DISTANCE = "distance"
        var KEY_ANGLE = "angle"
        var KEY_PROVIDER = "provider"
    }
}
